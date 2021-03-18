package com.kdyzm.spring.security.oauth.study.resource.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author kdyzm
 */
@Configuration
@EnableResourceServer
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "r1";

    // @Autowired
    // private ResourceServerTokenServices resourceServerTokenServices;

    // @Autowired
    // private TokenStore tokenStore;


    // 对称秘钥，资源服务器使用秘钥来验证
    private static final String SIGNING_KEY = "auth";

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);
        return jwtAccessTokenConverter;
    }

    // @Bean
    // public ResourceServerTokenServices resourceServerTokenServices(){
    //     RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
    //     remoteTokenServices.setCheckTokenEndpointUrl("http://127.0.0.1:30000/oauth/check_token");
    //     remoteTokenServices.setClientId("c1");
    //     remoteTokenServices.setClientSecret("secret");
    //     return remoteTokenServices;
    // }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .resourceId(RESOURCE_ID)
                // 采用jwt，注释掉此行和上面的resourceServerTokenServices，不再采用请求/oauth/check_token接口的方式，采用jwt方式传递token等信息，在TokenConfig类中配置的tokenStore
                // .tokenServices(resourceServerTokenServices)//令牌服务
                .tokenStore(tokenStore())
                .stateless(true);
    }


    /**
     * 这里仅仅对auth2.0的安全进行配置。这里的.antMatchers("/**").access("#oauth2.hasScope('all')")表示所有的请求携带的令牌都必须拥有all的授权范围，
     * 其中all授权范围必须和认证服务中的配置相一致。
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }


}
