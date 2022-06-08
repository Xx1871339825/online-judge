package com.crowoj.api.controller.admin;
import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.param.SignInParam;
import com.crowoj.api.service.MenuService;
import com.crowoj.api.service.UserAuthsService;
import com.crowoj.api.vo.JWTVO;
import com.crowoj.api.vo.ResultVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author crow
 * @create 2022/3/8 11:52
 * @description
 */
@RestController("adminAuthController")
@RequestMapping("admin/auth")
public class AuthController {

    @Resource
    private UserAuthsService userAuthsService;

    @Resource
    private MenuService menuService;

    @PostMapping("sign-in")
    public ResultVO<JWTVO> signIn(@RequestBody SignInParam param){
        return ResultUtil.success(userAuthsService.adminSignIn(param));
    }

    @GetMapping("menu-list")
    public ResultVO<Object> getMenuList(){
        var activeUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        return ResultUtil.success(menuService.findMenuByUid(activeUser.getUser().getUid()));
    }

    @GetMapping("refresh")
    public ResultVO<JWTVO> refresh(HttpServletRequest request){
        var token = request.getHeader("Authorization");
        if (token == null)
            throw new SimpleException(ResultEnum.UNAUTHORIZED_JWT_VALIDATION_EXCEPTION);
        token = token.replace("Bearer ","");
        return ResultUtil.success(userAuthsService.refresh(token,"admin"));
    }

}
