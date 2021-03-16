package com.kdyzm.spring.security.auth.center.config;

import cn.hutool.core.util.StrUtil;
import com.kdyzm.spring.security.auth.center.service.MyUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 自定义的登录账号密码验证Provider
 * 使密码在前端加密并传输，后端存入数据库，登录时只做equals校验
 *
 * @author tuess
 * @version 1.0
 **/
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private final MyUserDetailsServiceImpl userDetailService;

    @Autowired
    public LoginAuthenticationProvider(MyUserDetailsServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // http请求的账户密码
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // 数据库用户名查询出来的用户名和密码
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("用户名未找到");
        }

        if (!StrUtil.equals(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码不正确");
        }

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
