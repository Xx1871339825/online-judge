package com.crowoj.api.core.utils;

import com.crowoj.api.core.enums.IdentityType;

/**
 * @author crow
 * @create 2021/10/19 23:19
 * @description 验证工具
 */
public class ValidUtil {
    public static IdentityType validIdentityType(String identity){
        String check = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return identity.matches(check) ? IdentityType.EMAIL : IdentityType.USERNAME;
    }
}
