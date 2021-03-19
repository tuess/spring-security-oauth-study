package com.kdyzm.spring.security.oauth.study.resource.server.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 *
 * @author tuess
 * @version 1.0
 **/
@Component("redisUtils")
public class RedisUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 存放string类型
     *
     * @param key
     * @param data
     * @param timeout
     */
    public void setString(String key, String data, Long timeout) {
        if (timeout != null) {
            stringRedisTemplate.opsForValue().set(key, data, timeout, TimeUnit.SECONDS);
            stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        } else {
            stringRedisTemplate.opsForValue().set(key, data);
        }
    }

    /**
     * 存放string类型
     *
     * @param key
     * @param data
     */
    public void setString(String key, String data) {
        setString(key, data, null);
    }

    /**
     * 根据key查询string类型
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return value;
    }

    /**
     * 根据对应的key删除key
     *
     * @param key
     */
    public void delKey(String key) {
        stringRedisTemplate.delete(key);
    }
}
