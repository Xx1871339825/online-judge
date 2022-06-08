package com.crowoj.judgeserver.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author crow
 * @create 2022/2/18 22:13
 * @description
 */
@Component
public class JWTUtil {
    private static String ACCESS_SECRET_KEY;
    //单位：分钟
    private static long ACCESS_EXPIRE_TIME;
    //单位：分钟
    private static long REFRESH_EXPIRE_TIME;

    private static String REFRESH_SECRET_KEY;



    @Value("${utils.jwtUtils.accessSecretKey}")
    public void setAccessSecretKey(String accessSecretKey) {
        JWTUtil.ACCESS_SECRET_KEY = accessSecretKey;
    }

    @Value("${utils.jwtUtils.accessExpireTime}")
    public void setAccessExpireTime(long accessExpireTime) {
        JWTUtil.ACCESS_EXPIRE_TIME = accessExpireTime * 60 * 1000;
    }

    @Value("${utils.jwtUtils.refreshExpireTime}")
    public void setRefreshExpireTime(long refreshExpireTime) {
        JWTUtil.REFRESH_EXPIRE_TIME = refreshExpireTime * 60 * 1000;
    }

    @Value("${utils.jwtUtils.refreshSecretKey}")
    public void setRefreshSecretKey(String refreshSecretKey) {
        JWTUtil.REFRESH_SECRET_KEY = refreshSecretKey;
    }



    public static String createAccessJwt(Long userId, String uuid){
        return Jwts.builder()
                .setId(userId + "")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, ACCESS_SECRET_KEY)
                .claim("uuid",uuid)
                .compact();
    }

    public static String createRefreshJwt(Long userId, String uuid){
        return Jwts.builder()
                .setId(userId + "")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY)
                .claim("uuid",uuid)
                .compact();
    }

    public static Claims parseAccessToken(String token){
        return Jwts.parser()
                .setSigningKey(ACCESS_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static Claims parseRefreshToken(String token){
        return Jwts.parser()
                .setSigningKey(REFRESH_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }


}
