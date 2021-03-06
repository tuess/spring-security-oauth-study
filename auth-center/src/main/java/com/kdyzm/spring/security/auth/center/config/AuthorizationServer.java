package com.kdyzm.spring.security.auth.center.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author kdyzm
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("accessTokenConverter")
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    private CustomTokenEnhancer customTokenEnhancer;

    /**
     * ????????????token
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore);
        // token???refreshToken???????????????????????????????????????????????????????????????token????????????????????????????????????
        services.setAccessTokenValiditySeconds(7200);
        services.setRefreshTokenValiditySeconds(259200);

        // ??????????????????jwt
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        // ????????????jwt
        // tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(jwtAccessTokenConverter));
        // ?????????????????????jwt
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer, jwtAccessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);

        return services;
    }

    /**
     * ?????????????????????????????????????????????????????????redis?????????????????????????????????????????????????????????
     * ????????????????????????????????????????????????dataSource?????????ClientDetailsServiceConfigurer???Autowired???????????????????????????????????????
     * ????????????redis????????????????????????????????????????????????????????????
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new RedisAuthenticationCodeServices(); //?????????redis?????????????????????????????????
        // return new JdbcAuthorizationCodeServices(dataSource); //??????????????????????????????????????????
    }

    /**
     * ???????????????????????????
     * ClientDetailsService??????????????????????????????????????????????????????
     * ????????????????????????????????????????????????????????????????????????inMemory?????????????????????????????????
     * ?????????????????????????????????token????????????????????????????????????
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // clients.jdbc(dataSource); //???????????????????????????
        clients.inMemory()
                .withClient("c1")
                .secret(new BCryptPasswordEncoder().encode("secret"))//$2a$10$0uhIO.ADUFv7OQ/kuwsC1.o3JYvnevt5y3qX/ji0AUXs4KYGio3q6
                .resourceIds("r1")
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                .scopes("all")
                .autoApprove(false)
                .redirectUris("https://www.baidu.com");
    }


    /**
     * ???????????????????????????????????????
     * ?????????????????????tokenStore()?????????????????????????????????redis????????????jwt
     * ???????????????accessTokenConverter???JwtAccessTokenConverter??????????????????jwt?????????????????????InMemory
     * ?????????tokenServices()????????????????????????tokenStore()???tokenStore()????????????JwtAccessTokenConverter
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager) //?????????????????????????????????????????????????????????password???????????????????????????????????????????????????????????? AuthenticationManager ?????????
                .authorizationCodeServices(authorizationCodeServices) //??????????????????????????????????????????????????? AuthorizationCodeServices ????????????????????????????????? "authorization_code" ????????????????????????
                .tokenServices(tokenServices()) //?????????????????????????????????
                .allowedTokenEndpointRequestMethods(HttpMethod.POST); //?????????????????????????????????

        //???????????????????????????????????????????????????????????????????????????????????????controller??????????????????oauth????????????????????????
        endpoints.pathMapping("/oauth/confirm_access", "/custom/confirm_access");
    }

    /**
     * ???????????????????????????????????????
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("isAuthenticated()") //tokenkey??????endpoint?????????JwtToken??????????????????????????????????????????????????????????????????????????????????????????endpoint???????????????????????????????????????????????????
                .checkTokenAccess("isAuthenticated()") //checkToken??????endpoint??????????????????????????????????????????????????????????????????????????????token?????????????????????????????????
                .allowFormAuthenticationForClients(); //????????????form???????????????????????????(client_id,client_secret),?????????basic????????????
    }


}
