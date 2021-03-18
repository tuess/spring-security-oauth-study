package com.kdyzm.spring.security.oauth.study.resource.server.config;

import com.kdyzm.spring.security.oauth.study.resource.server.handler.AccessDecisionManagerImpl;
import com.kdyzm.spring.security.oauth.study.resource.server.handler.AccessSecurityMetadataSource;
import com.kdyzm.spring.security.oauth.study.resource.server.handler.CustomizeAbstractSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author kdyzm
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessDecisionManagerImpl accessDecisionManager;

    @Autowired
    private AccessSecurityMetadataSource securityMetadataSource;

    @Autowired
    private CustomizeAbstractSecurityInterceptor securityInterceptor;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    //静态资源配置，不用登录即可访问
    @Override
    public void configure(WebSecurity web) {
        //设置哪些资源可以不登录直接访问，例如swagger2所需要用到的静态资源
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-resources/configuration/ui",
                "/swagger-resources",
                "/swagger-resources/configuration/security",
                "/swagger-ui.html", "/**/r3");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 设置哪些url可以被登录后的所有人访问
        http.authorizeRequests().antMatchers("/**/r3").permitAll();
        http.csrf()
                .disable()
                .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(accessDecisionManager);//决策管理器
                        o.setSecurityMetadataSource(securityMetadataSource);//安全元数据源
                        return o;
                    }
                });
        // 添加自定义的过滤器
        // .and().addFilterAfter(new PermissionFilter(), FilterSecurityInterceptor.class);
        //自定义的一套url权限拦截器增加到默认拦截链中
        http.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);

        // .antMatchers("/r1").hasAuthority("p2")
        // .antMatchers("/r2").hasAuthority("p2")
        // .antMatchers("/**").authenticated();//所有的/**请求必须通过认证
        // .anyRequest().permitAll();//其它所有请求都可以随意访问
    }
}
