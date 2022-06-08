package com.crowoj.api.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.crowoj.api.core.enums.IdentityType;
import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.utils.MessageUtils;
import com.crowoj.api.dao.UserAuthsDao;
import com.crowoj.api.entity.UserAuths;
import com.crowoj.api.service.AsyncService;
import com.crowoj.api.service.EmailService;
import com.crowoj.api.service.RedisService;
import com.crowoj.api.service.UserAuthsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author crow
 * @create 2021/10/25 11:31
 * @description
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private UserAuthsDao userAuthsDao;

    @Resource
    private RedisService redisService;

    @Resource
    private AsyncService asyncService;

    @Value("${validCode.expireTime}")
    private long CODE_EXPIRE_MINUTE;
    @Value("${spring.mail.username}")
    private String SENDER;


    @Override
    public void sendSignUpEmailValidCoe(String email,String ip) {
        var uaQw = new QueryWrapper<UserAuths>();
        uaQw.lambda().eq(UserAuths::getIdentifier,email).eq(UserAuths::getIdentityType,IdentityType.EMAIL);
        var ua = userAuthsDao.selectOne(uaQw);
        if (ua != null) {
            throw new SimpleException(ResultEnum.BAD_REQUEST_EMAIL_ALREADY_EXISTS);
        }
        // 开启1min限制
        redisService.setString(ip+ "sign-up/email","1",1);
        sendEmailValidCode(email);
    }

    @Override
    public void sendUpdateEmailValidCode(String email) {
        sendEmailValidCode(email);
    }

    private void sendEmailValidCode(String email) {
        var message = new SimpleMailMessage();
        var validCode = RandomUtil.randomString(6);
        message.setSubject(MessageUtils.get("email.subject"));
        message.setText(MessageUtils.get("email.text", validCode, CODE_EXPIRE_MINUTE + ""));
        message.setFrom(SENDER);
        message.setTo(email);
        redisService.setString(email,validCode,CODE_EXPIRE_MINUTE);
        asyncService.asyncSenderEmail(message);
    }

    @Override
    public void valid(String email, String emailValidCode) {
        var validCode = redisService.getString(email);
        if (!StrUtil.isAllNotBlank(validCode,email) || !emailValidCode.equals(validCode))
            throw new SimpleException(ResultEnum.FORBIDDEN.getStatus(),MessageUtils.get("email.valid.error"));
        redisService.del(email);
    }
}
