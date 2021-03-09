package com.kdyzm.spring.security.oauth.study.resource.server.config;

import com.kdyzm.spring.security.oauth.study.resource.server.filter.PermissionFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .access("@rbacPermission.hasPermission(request, authentication)")
                .and().exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and().addFilterAfter(new PermissionFilter(), FilterSecurityInterceptor.class);

        // .antMatchers("/r1").hasAuthority("p2")
        // .antMatchers("/r2").hasAuthority("p2")
        // .antMatchers("/**").authenticated();//所有的/**请求必须通过认证
        // .anyRequest().permitAll();//其它所有请求都可以随意访问
    }
}
