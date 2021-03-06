package com.kdyzm.spring.security.auth.center.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // @Autowired
    // private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    //配置认证管理器
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private LogOutHandler logOutHandler;

    @Autowired
    private LogInSuccessHandler logInSuccessHandler;

    //配置密码编码器，默认为BCrypt加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //安全拦截机制（最重要）
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login*", "/css/*", "/test").permitAll()
                .anyRequest().authenticated()
                // .and()
                // .oauth2Login()
                // .formLogin()
                // .loginPage("/login.html")
                // .loginProcessingUrl("/login")
                // .successHandler(logInSuccessHandler)
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(logOutHandler);
        // 自定义的登录失败页面
        // .failureHandler(myAuthenticationFailureHandler);

    }


}
