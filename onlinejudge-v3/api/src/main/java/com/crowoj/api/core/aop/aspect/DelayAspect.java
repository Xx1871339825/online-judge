package com.crowoj.api.core.aop.aspect;

import cn.hutool.core.util.StrUtil;
import com.crowoj.api.core.aop.Delay;
import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.utils.IpUtil;
import com.crowoj.api.core.utils.JWTUtil;
import com.crowoj.api.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author xiaoxing
 * @Description
 * @create 2021/5/10
 */
@Aspect
@Component
@ComponentScan("com.crowoj.api.core.aop.*")
@Slf4j
public class DelayAspect {

    @Resource
    RedisService redisService;


    private boolean startDelay(String key,int time) {
        String string = redisService.getString(key);
        if (StrUtil.isBlank(string)){
            if (!key.contains("sign-up/email")){
                redisService.setString(key,time+"",time);
            }
            return true;
        }

        return false;
    }

    @Pointcut("@annotation(com.crowoj.api.core.aop.Delay)")
    private void verifyDelay(){
    }

    @Before("verifyDelay() && @annotation(delay)")
    public void doBasicProfiling(Delay delay) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = IpUtil.getIpAddress( request );//获取ip地址
        if (StrUtil.contains(delay.path(),"refresh")){
            JWTUtil.parseRefreshToken(request.getHeader("Authorization").replace("Bearer ",""));
        }
        if (!startDelay(ip+delay.path(),delay.time()))
            throw new SimpleException(ResultEnum.INTERNAL_SERVER_ERROR_SERVER_BUSY);
    }



}
