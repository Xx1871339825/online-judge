package com.crowoj.api.core.shiro;

import cn.hutool.core.util.StrUtil;
import com.crowoj.api.core.enums.BanFlag;
import com.crowoj.api.core.enums.MenuType;
import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.exception.MyAuthenticationException;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.utils.JWTUtil;
import com.crowoj.api.core.utils.MessageUtils;
import com.crowoj.api.entity.Menu;
import com.crowoj.api.service.MenuService;
import com.crowoj.api.service.RedisService;
import com.crowoj.api.service.RoleService;
import com.crowoj.api.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * @author crow
 * @create 2021/10/21 18:27
 * @description
 */
@Service
@AllArgsConstructor
public class SimpleRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;


    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JWTToken;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。(认证方法)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        /*解析Token*/
        var token = (JWTToken)authenticationToken;
        Claims claims;
        try {
            claims = JWTUtil.parseAccessToken(token.getToken());
        }catch (Exception e){
            e.printStackTrace();
            if (e instanceof ExpiredJwtException){
                throw new MyAuthenticationException(ResultEnum.UNAUTHORIZED_JWT_EXPIRE);
            }else if (e instanceof MalformedJwtException){
                throw new MyAuthenticationException(ResultEnum.UNAUTHORIZED_JWT_VALIDATION_EXCEPTION);
            }else if (e instanceof SignatureException){
                throw new MyAuthenticationException(ResultEnum.UNAUTHORIZED_JWT_SIGNATURE_EXCEPTION);
            }else {
                throw new MyAuthenticationException(ResultEnum.UNAUTHORIZED);
            }
        }
        var uid = claims.getId();
        var user = userService.getUserByUid(Long.valueOf(uid));
        if (Objects.equals(user.getBanFlag().getBanNo(), BanFlag.IS_BAN.getBanNo()))
            throw new MyAuthenticationException(ResultEnum.FORBIDDEN);

        var activeUser = new ActivityUser();
        activeUser.setUser(user);
        // 判断uuid
        var uuid = claims.get("uuid",String.class);
        //判断是否token黑名单
        var block = redisService.getString(uuid);
        if (StrUtil.isNotEmpty(block)){
            throw new MyAuthenticationException(ResultEnum.UNAUTHORIZED_JWT_INVALID);
        }

        if (claims.get("identity",String.class).equals("admin")){
            //管理员用户，添加权限
            var roleList = roleService.findRoleByUid(user.getUid());
            var menuList = menuService.findMenuByUid(user.getUid());
            var urls = new HashSet<String>();
            var perms = new HashSet<String>();
            for (Menu menu : menuList) {
                var url = menu.getMenuPath();
                var perm = menu.getPermissions();
                urls.add(url);
                perms.add(perm);
            }
            activeUser.setUrls(urls)
                    .setMenus(menuList)
                    .setRoles(roleList)
                    .setPermissions(perms);
        }

        return new SimpleAuthenticationInfo(activeUser,token.getToken(),getName());
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的,(授权方法)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        var simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        var activeUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        var roleList = activeUser.getRoles();
        if (roleList == null){
            throw new MyAuthenticationException(ResultEnum.UNAUTHORIZED);
        }
        simpleAuthorizationInfo.addStringPermissions(activeUser.getPermissions());
        roleList.forEach(role -> {
            simpleAuthorizationInfo.addRole(role.getRoleName());
        });
        return simpleAuthorizationInfo;
    }

    /**/



}
