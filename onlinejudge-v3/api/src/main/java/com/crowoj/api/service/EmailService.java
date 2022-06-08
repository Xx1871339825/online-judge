package com.crowoj.api.service;


/**
 * @author crow
 * @create 2021/10/25 11:31
 * @description
 */
public interface EmailService {

    void sendSignUpEmailValidCoe(String email,String ip);

    void sendUpdateEmailValidCode(String email);

//    void sendEmailValidCode(String email);

    void valid(String email, String emailValidCode);
}
