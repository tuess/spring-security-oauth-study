package com.kdyzm.spring.security.oauth.study.resource.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 用来进行url权限校验的类
 *
 * @author tuess
 * @version 1.0
 **/
@Slf4j
@Component("rbacPermission")
public class RbacPermission {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean hasPermission = false;
        log.debug("-------url:{}", request.getRequestURI());
        // if (principal instanceof UserDetails) {
        // 读取用户所拥有的权限菜单
        for (GrantedAuthority authority : authorities) {
            System.out.println(authority.getAuthority());
            System.out.println(authority.toString());
            if (antPathMatcher.match(authority.getAuthority(), request.getRequestURI())) {
                hasPermission = true;
                break;
            }
        }
        // }
        return hasPermission;
    }
}
