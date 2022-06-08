package com.crowoj.api.controller;
import com.crowoj.api.core.aop.Delay;
import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.utils.IpUtil;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.param.EmailParam;
import com.crowoj.api.param.ForgetPasswordParam;
import com.crowoj.api.param.SignInParam;
import com.crowoj.api.param.SignUpParam;
import com.crowoj.api.service.EmailService;
import com.crowoj.api.service.UserAuthsService;
import com.crowoj.api.vo.JWTVO;
import com.crowoj.api.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author crow
 * @create 2021/10/19 19:59
 * @description Sign in：登录 Sign out：注销 Sign up：注册
 */
@Api(tags = "用户authApi")
@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Resource
    private UserAuthsService userAuthsService;
    @Resource
    private EmailService emailService;

    @ApiOperation(value = "刷新Token接口，需要使用refreshToken",notes = "如果检测到refreshToken的过期时间小于一个小时，会返回一个新的refreshToken")
    @GetMapping("refresh")
    public ResultVO<JWTVO> refresh(HttpServletRequest request){
        var token = request.getHeader("Authorization");
        if (token == null)
            throw new SimpleException(ResultEnum.UNAUTHORIZED_JWT_VALIDATION_EXCEPTION);
        token = token.replace("Bearer ","");
        return ResultUtil.success(userAuthsService.refresh(token,""));
    }

    @ApiOperation("登录接口")
    @PostMapping("sign-in")
    public ResultVO<JWTVO> signIn(@RequestBody @Valid SignInParam signInParam){
        return ResultUtil.success(userAuthsService.signIn(signInParam));
    }

    @ApiOperation("注册")
    @PostMapping("sign-up")
    public ResultVO<JWTVO> signUp(@RequestBody @Valid SignUpParam signUpParam){
        return ResultUtil.success(userAuthsService.sinUp(signUpParam));
    }

    @ApiOperation("注册时获取邮箱验证码")
    @Delay(path = "sign-up/email")
    @PostMapping("sign-up/email")
    public ResultVO<Object> sendSignUpEmail(@Valid @RequestBody EmailParam emailParam,HttpServletRequest request){
        String ipAddress = IpUtil.getIpAddress(request);
        emailService.sendSignUpEmailValidCoe(emailParam.getEmail(),ipAddress);
        return ResultUtil.success();
    }



    @Delay(path = "update/email/{email}")
    @PostMapping("update/email/{email}")
    public ResultVO<Object> sendUpdateEmail(@PathVariable String email){
        //发送更新用的Email
        emailService.sendUpdateEmailValidCode(email);
        return ResultUtil.success();
    }

    @PostMapping("forget")
    public ResultVO<Boolean> forgetPassword(@RequestBody @Valid ForgetPasswordParam param){
        return ResultUtil.success(userAuthsService.forgetPassword(param));
    }

//    @Autowired
//    RestTemplate restTemplate;
//    @GetMapping("deleteAll")
//    public void deleteSandBoxFile(){
//        var url = "http://1.15.125.45:5050/file";
//        var map = restTemplate.getForEntity(url, Map.class);
//        map.getBody().forEach((key,value) -> {
//            restTemplate.delete(url + "/" + key);
//        });
//    }
}
