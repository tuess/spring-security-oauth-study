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
     * 配置管理token
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore);
        // token和refreshToken失效时间，如果客户端信息存放在数据库，那么token失效时间就要去数据库配置
        services.setAccessTokenValiditySeconds(7200);
        services.setRefreshTokenValiditySeconds(259200);

        // 此处配置使用jwt
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        // 使用普通jwt
        // tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(jwtAccessTokenConverter));
        // 添加自定义增强jwt
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer, jwtAccessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);

        return services;
    }

    /**
     * 授权码模式中的授权码存放方式，此处采用redis存放，也可以存放到数据库，前提是创建表
     * 如果选择存放至数据库，那么这里的dataSource不能和ClientDetailsServiceConfigurer用Autowired注入的，要用方法传参进来的
     * 由于使用redis存放不需要这个参数，建议就写在方法参数里
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new RedisAuthenticationCodeServices(); //存放到redis，需要自己写一个配置类
        // return new JdbcAuthorizationCodeServices(dataSource); //存放到数据库，需要手动创建表
    }

    /**
     * 配置客户端详细信息
     * ClientDetailsService只有存在内存中和存到数据库中两种方式
     * 存放到数据库中就需要自行去数据库添加好数据，不像inMemory这么方便，可以直接配置
     * 如果存放到数据库，那么token失效时间就要去数据库配置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // clients.jdbc(dataSource); //配置为存放到数据库
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
     * 配置令牌和令牌访问端点服务
     * 此处也可以通过tokenStore()设置为存放在数据库或者redis或者使用jwt
     * 如果配置了accessTokenConverter为JwtAccessTokenConverter，那么就使用jwt，否则默认使用InMemory
     * 这里的tokenServices()已经在上面配置了tokenStore()，tokenStore()中配置了JwtAccessTokenConverter
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager) //认证管理器，当你选择了资源所有者密码（password）授权类型的时候，请设置这个属性注入一个 AuthenticationManager 对象。
                .authorizationCodeServices(authorizationCodeServices) //这个属性是用来设置授权码服务的（即 AuthorizationCodeServices 的实例对象），主要用于 "authorization_code" 授权码类型模式。
                .tokenServices(tokenServices()) //加载上面配置的管理令牌
                .allowedTokenEndpointRequestMethods(HttpMethod.POST); //配置令牌端点的访问方式

        //可以将默认的端点替换成自定义的端点，前提是必须有手写的这个controller（此处重写了oauth的确认授权页面）
        endpoints.pathMapping("/oauth/confirm_access", "/custom/confirm_access");
    }

    /**
     * 用来配置令牌端点的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("isAuthenticated()") //tokenkey这个endpoint当使用JwtToken且使用非对称加密时，资源服务用于获取公钥而开放的，这里指这个endpoint允许已授权用户访问，默认不允许访问
                .checkTokenAccess("isAuthenticated()") //checkToken这个endpoint允许已授权用户访问，如果不公开则所有接口请求时的校验token会出错，默认不允许访问
                .allowFormAuthenticationForClients(); //允许通过form提交客户端认证信息(client_id,client_secret),默认为basic方式认证
    }


}
