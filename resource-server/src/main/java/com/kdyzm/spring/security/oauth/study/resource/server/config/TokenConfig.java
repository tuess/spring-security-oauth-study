package com.kdyzm.spring.security.oauth.study.resource.server.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author kdyzm
 */
@Configuration
public class TokenConfig {

    // 对称秘钥，资源服务器使用秘钥来验证
    // private static final String SIGNING_KEY = "auth";
    //
    // @Bean
    // public TokenStore tokenStore() {
    //     return new JwtTokenStore(accessTokenConverter());
    // }
    //
    // @Bean
    // public JwtAccessTokenConverter accessTokenConverter() {
    //     JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    //     jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);
    //     return jwtAccessTokenConverter;
    // }
}
