package com.crowoj.api.service.impl;

import com.crowoj.api.service.AsyncService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author crow
 * @create 2021/10/25 11:30
 * @description
 */
@Service
@Async
public class AsyncServiceImpl implements AsyncService {

    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public void asyncSenderEmail(SimpleMailMessage message) {
        javaMailSender.send(message);
    }

}
