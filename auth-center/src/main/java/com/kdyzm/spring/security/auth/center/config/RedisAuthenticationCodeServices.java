package com.kdyzm.spring.security.auth.center.config;

import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import java.nio.charset.StandardCharsets;

/**
 * 用户授权码模式将授权码存储到redis中去
 *
 * @author tuess
 * @version 1.0
 **/
@Slf4j
public class RedisAuthenticationCodeServices extends RandomValueAuthorizationCodeServices {
    private static final String AUTH_CODE_KEY = "auth_code";

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Override
    protected OAuth2Authentication remove(String code) {
        RedisConnection conn = getConnection();
        try {
            OAuth2Authentication authentication = null;
            try {
                authentication = (OAuth2Authentication) SerializationUtils
                        .deserialize(conn.hGet(AUTH_CODE_KEY.getBytes(StandardCharsets.UTF_8), code.getBytes(StandardCharsets.UTF_8)));
            } catch (Exception e) {
                return null;
            }
            if (null != authentication) {
                conn.hDel(AUTH_CODE_KEY.getBytes(StandardCharsets.UTF_8), code.getBytes(StandardCharsets.UTF_8));
            }

            return authentication;
        } catch (Exception e) {
            return null;
        } finally {
            conn.close();
        }
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        RedisConnection conn = getConnection();
        try {
            conn.hSet(AUTH_CODE_KEY.getBytes(StandardCharsets.UTF_8), code.getBytes(StandardCharsets.UTF_8),
                    SerializationUtils.serialize(authentication));
            log.debug("存储授权码到redis成功,{}", AUTH_CODE_KEY.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("保存authentication code 失败", e);
        } finally {
            conn.close();
        }

    }

    private RedisConnection getConnection() {
        return redisConnectionFactory.getConnection();
    }

}