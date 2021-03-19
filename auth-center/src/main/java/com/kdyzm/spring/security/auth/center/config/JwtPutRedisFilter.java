// package com.kdyzm.spring.security.auth.center.config;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.oauth2.common.OAuth2AccessToken;
// import org.springframework.security.oauth2.provider.OAuth2Authentication;
// import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
// import org.springframework.security.oauth2.provider.token.TokenStore;
//
// /**
//  * @author tuess
//  * @version 1.0
//  **/
//
// @Configuration
// public class JwtPutRedisFilter extends DefaultTokenServices {
//
//     @Autowired
//     @Override
//     public void setTokenStore(TokenStore tokenStore) {
//         super.setTokenStore(tokenStore);
//     }
//
//
//
//     @Override
//     @Bean
//     public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
//         System.out.println("aaaaaaaaaaaaaaaa");
//         return super.token.getAccessToken(authentication);
//     }
// }
