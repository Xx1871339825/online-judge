package com.crowoj.api.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author crow
 * @create 2021/10/19 18:12
 * @description Jwt工具类
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



    public static String createAccessJwt(Long userId, String uuid,String identity){
        var jwt =  Jwts.builder()
                .setId(userId + "")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .claim("uuid",uuid)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, ACCESS_SECRET_KEY);
        if (identity.equals("admin")){
            jwt.claim("identity","admin");
        }else {
            jwt.claim("identity","user");

        }
        return jwt.compact();
    }

    public static String createRefreshJwt(Long userId, String uuid,String identity){
        var jwt = Jwts.builder()
                .setId(userId + "")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY)
                .claim("uuid",uuid);
        if (identity.equals("admin")){
            jwt.claim("identity","admin");
        }else {
            jwt.claim("identity","user");
        }
        return jwt.compact();
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
