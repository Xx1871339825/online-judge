package com.crowoj.api.core.utils;

import cn.hutool.crypto.digest.BCrypt;

/**
 * @author crow
 * @create 2021/10/19 23:05
 * @description Bcrypt加密和匹配
 */
public class BcryptUtil {

    public static String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean match(String password, String encodePassword){
        return BCrypt.checkpw(password,encodePassword);
    }
}
