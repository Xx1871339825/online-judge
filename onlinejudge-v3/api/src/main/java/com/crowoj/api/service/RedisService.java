package com.crowoj.api.service;

/**
 * @author crow
 * @create 2021/10/25 1:21
 * @description
 */
public interface RedisService {
    boolean setString(String key, String value);

    //把字符串类型的value写入缓存（有失效时间）

    boolean setString(String key, String value, long time);

    //将value以json格式写入缓存()

    boolean setObject(String key, Object value, long time);

    //获取缓存String

    String getString(String key);

    //获取缓存json对象

    <T> T getObj(String key, Class<T> clazz);

//删除缓存

    boolean del(String key);
}
