package com.kdyzm.spring.security.oauth.study.resource.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * 自定义鉴权管理器，根据URL资源权限和用户角色权限进行鉴权
 *
 * @author tuess
 * @version 1.0
 **/
@Service
@Slf4j
public class AccessDecisionManagerImpl implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        log.info("[用户允许请求的地址]: {}", authentication.getAuthorities());

        String resourceRole = ((FilterInvocation) o).getHttpRequest().getRequestURI();
        log.info("[资源地址]: {}", resourceRole);
        for (GrantedAuthority userAuth : authentication.getAuthorities()) {
            if (antPathMatcher.match(userAuth.getAuthority().trim(), resourceRole.trim())) {
                return;
            }
        }
        throw new AccessDeniedException("权限不足");

    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
