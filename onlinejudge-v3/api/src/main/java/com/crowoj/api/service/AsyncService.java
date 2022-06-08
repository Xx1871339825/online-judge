package com.crowoj.api.service;

import org.springframework.mail.SimpleMailMessage;

/**
 * @author crow
 * @create 2021/10/25 11:27
 * @description
 */
public interface AsyncService {
    void asyncSenderEmail(SimpleMailMessage message);
}
