package com.crowoj.api.service.impl;

import cn.hutool.json.JSONUtil;
import com.crowoj.api.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Optional;

/**
 * @author crow
 * @create 2021/10/25 1:21
 * @description
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Value("${redis.prefix}")
    private String prefix;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private String buildRedisKey(String key) {
        return prefix + key;
    }



    @Override
    public boolean setString(String key, String value) {
        return setString(buildRedisKey(key), value,0);
    }

    @Override
    public boolean setString(String key, String value, long time) {
        if (time > 0){
            stringRedisTemplate.opsForValue().set(buildRedisKey(key),value,Duration.ofMinutes(time));

        }else {
            stringRedisTemplate.opsForValue().set(buildRedisKey(key),value);
        }
        return true;
    }

    @Override
    public boolean setObject(String key, Object value, long time) {
        if (time > 0){
            setString(buildRedisKey(key), JSONUtil.toJsonStr(value),time);
        }else {
            setString(buildRedisKey(key),JSONUtil.toJsonStr(value), time);
        }
        return true;
    }

    @Override
    public String getString(String key) {
        return stringRedisTemplate.boundValueOps(buildRedisKey(key)).get();
    }

    @Override
    public <T> T getObj(String key, Class<T> clazz) {
        return JSONUtil.toBean(getString(buildRedisKey(key)),clazz);
    }

    @Override
    public boolean del(String key) {
        var optional = Optional.ofNullable(stringRedisTemplate.delete(buildRedisKey(key)));
        return optional.orElse(false);
    }
}
