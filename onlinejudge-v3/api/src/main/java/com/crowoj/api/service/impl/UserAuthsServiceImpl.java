package com.crowoj.api.service.impl;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.core.enums.BanFlag;
import com.crowoj.api.core.enums.IdentityType;
import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.utils.BcryptUtil;
import com.crowoj.api.core.utils.JWTUtil;
import com.crowoj.api.core.utils.PrivacyStringUtil;
import com.crowoj.api.core.utils.ValidUtil;
import com.crowoj.api.entity.Menu;
import com.crowoj.api.entity.User;
import com.crowoj.api.entity.UserAuths;
import com.crowoj.api.param.*;
import com.crowoj.api.dao.UserAuthsDao;
import com.crowoj.api.service.*;
import com.crowoj.api.vo.JWTVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author crow
 * @create 2021/10/19 20:16
 * @description
 */
@Slf4j
@Service
public class UserAuthsServiceImpl extends ServiceImpl<UserAuthsDao,UserAuths> implements UserAuthsService {
    @Value("${refreshExpireTimeDifference}")
    long timeDifference;

    @Resource
    private UserService userService;

    @Resource
    private EmailService emailService;

    @Resource
    private RedisService redisService;

    @Override
    public JWTVO adminSignIn(SignInParam param) {
        var userAuths = getSignInUserAuths(param);
        var uuid = UUID.randomUUID().toString();
        var user = userService.getUserByUid(userAuths.getUid());
        if (user.getBanFlag().equals(BanFlag.IS_BAN)){
            throw new SimpleException(ResultEnum.UNAUTHORIZED);
        }
        var accessToken = JWTUtil.createAccessJwt(user.getUid(),uuid,"admin");
        var refreshToken = JWTUtil.createRefreshJwt(user.getUid(),uuid,"admin");
        return new JWTVO().setUser(user)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }


    @Override
    public JWTVO signIn(SignInParam signInParam) {
        //根据用户输入信息检索出用户登录信息,如果查询不到用户信息则抛出异常用户名或密码不存在
        var userAuths = getSignInUserAuths(signInParam);
        //返回用户信息、访问令牌和刷新令牌
        var uuid = UUID.randomUUID().toString();
        return new JWTVO().setAccessToken(JWTUtil.createAccessJwt(userAuths.getUid(),uuid,""))
                .setRefreshToken(JWTUtil.createRefreshJwt(userAuths.getUid(), uuid,""))
                .setUser(userService.getUserByUid(userAuths.getUid()));
    }

    @Override
    @Transactional
    public JWTVO sinUp(SignUpParam signUpParam) {

        var user = new User().setNickname(PrivacyStringUtil.maskEmail(signUpParam.getEmail()));
        //填写user
        userService.saveOrUpdateUser(user);

        //userAuths
        var userAuths = new UserAuths().setIdentityType(IdentityType.EMAIL)
                .setIdentifier(signUpParam.getEmail())
                .setUid(user.getUid())
                .setCredential(BcryptUtil.encode(signUpParam.getPassword()));

        var userAuths1 = new UserAuths().setIdentityType(IdentityType.USERNAME)
                .setIdentifier(signUpParam.getUsername())
                .setUid(user.getUid())
                .setCredential(BcryptUtil.encode(signUpParam.getPassword()));

        var userAuthList = new ArrayList<UserAuths>();
        userAuthList.add(userAuths);
        userAuthList.add(userAuths1);


        //验证邮箱验证码
        emailService.valid(signUpParam.getEmail(),signUpParam.getEmailValidCode());
        if (!saveBatch(userAuthList))
            throw new SimpleException(ResultEnum.INTERNAL_SERVER_ERROR_NOT_ALL_INSERT);

        var uuid = UUID.randomUUID().toString();

        return new JWTVO().setUser(user)
                .setAccessToken(JWTUtil.createAccessJwt(user.getUid(),uuid,""))
                .setRefreshToken(JWTUtil.createRefreshJwt(user.getUid(),uuid,""));
    }

//    @Override
//    public UserAuths getUserAuthsByIdentifierAndIdentityType(String identifier, IdentityType identityType) {
//        var uaQw = new QueryWrapper<UserAuths>();
//        uaQw.lambda().eq(UserAuths::getIdentifier,identifier).eq(UserAuths::getIdentityType,identityType);
//        return getOne(uaQw);
//    }

    @Override
    public JWTVO refresh(String token,String identity) {
        //验证token
        Claims claims = JWTUtil.parseRefreshToken(token);
        //如果为管理员的令牌则不允许通过该方法刷新
        if (identity.equals("admin")){
            var auth = claims.get("identity",String.class);
            if (!auth.equals(identity))
                throw new SimpleException(ResultEnum.UNAUTHORIZED);
        }
        var jwt = new JWTVO();
        var uuid = claims.get("uuid",String.class);
        //判断是否token黑名单
        var block = redisService.getString(uuid);
        if (StrUtil.isNotEmpty(block))
            throw new SimpleException(ResultEnum.UNAUTHORIZED_JWT_INVALID);
        // 获取过期时间戳
        var expirationTimestamp = claims.getExpiration().getTime();
        // 过期时间与当前时间判断，若refreshToken过期时间小于一个小时，则颁发新的refreshToken
        var currentTimestamp = System.currentTimeMillis();
        var timeDifference = expirationTimestamp - currentTimestamp;
        jwt.setAccessToken(JWTUtil.createAccessJwt(Long.valueOf(claims.getId()),uuid,identity));
        if (timeDifference < 1000 * 60 * 60 * timeDifference){
            // 过期时间小于timeDifference个小时，颁发refreshToken和accessToken
            jwt.setRefreshToken(JWTUtil.createRefreshJwt(Long.valueOf(claims.getId()),uuid,identity));
        }
        return jwt;
    }

    @Transactional
    @Override
    public Boolean updatePwd(UpdatePwdParam param, User user) {
        var qw = new QueryWrapper<UserAuths>();
        qw.lambda().eq(UserAuths::getIdentityType,IdentityType.EMAIL).eq(UserAuths::getUid,user.getUid());
        var auth = getOne(qw);
        if (auth == null)
            throw new SimpleException(ResultEnum.NOT_FOUND);
        var code = redisService.getString(auth.getIdentifier());
        emailService.valid(auth.getIdentifier(),code);
        //修改密码
        var uw = new UpdateWrapper<UserAuths>();
        uw.lambda().eq(UserAuths::getUid,user.getUid())
                .and(i -> i.eq(UserAuths::getIdentityType,IdentityType.EMAIL).or().eq(UserAuths::getIdentityType,IdentityType.USERNAME))
                .set(UserAuths::getCredential,BcryptUtil.encode(param.getNewPwd()));
        return update(uw);
    }

    @Transactional
    @Override
    public Boolean updateEmail(UpdateEmailParam param, User user) {
        emailService.valid(param.getEmail(),param.getCode());
        //验证是否存在
        var qw = new QueryWrapper<UserAuths>();
        qw.lambda().eq(UserAuths::getIdentityType,IdentityType.EMAIL)
                .eq(UserAuths::getIdentifier,param.getEmail());
        var count = count(qw);
        if (count != 0)
            throw new SimpleException(ResultEnum.BAD_REQUEST_EMAIL_ALREADY_EXISTS);

        var uw = new UpdateWrapper<UserAuths>();
        uw.lambda().eq(UserAuths::getUid,user.getUid())
                .eq(UserAuths::getIdentityType,IdentityType.EMAIL)
                .set(UserAuths::getIdentifier,param.getEmail());
        return update(uw);
    }

    @Override
    public Boolean forgetPassword(ForgetPasswordParam param) {
        emailService.valid(param.getEmail(),param.getEmailValid());

        var qw = new QueryWrapper<UserAuths>();
        qw.lambda().eq(UserAuths::getIdentifier,param.getEmail());
        var ua = getOne(qw);
        var uw = new UpdateWrapper<UserAuths>()
                .lambda().eq(UserAuths::getUid,ua.getUid())
                .set(UserAuths::getCredential,BcryptUtil.encode(param.getPassword()));
        update(uw);
        return true;
    }

    private UserAuths getSignInUserAuths(SignInParam signInParam){
        if (!StrUtil.isAllNotBlank(signInParam.getIdentifier(),signInParam.getPassword()))
            throw new SimpleException(ResultEnum.NOT_FOUND);
        //按账号和账号类型查询出ua
        var uaQw = new QueryWrapper<UserAuths>();
        uaQw.lambda().eq(UserAuths::getIdentifier,signInParam.getIdentifier()).eq(UserAuths::getIdentityType,ValidUtil.validIdentityType(signInParam.getIdentifier()));
        var userAuths = getOne(uaQw);
        if (userAuths==null)
            throw new SimpleException(ResultEnum.USER_NOT_FOUND);
        //判断密码是否匹配
        if (!BcryptUtil.match(signInParam.getPassword(),userAuths.getCredential()))
            throw new SimpleException(ResultEnum.UNAUTHORIZED_USERNAME_OR_PWD_ERROR);
        return userAuths;
    }


}
