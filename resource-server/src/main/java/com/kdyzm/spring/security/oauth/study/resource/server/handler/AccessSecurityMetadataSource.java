package com.kdyzm.spring.security.oauth.study.resource.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 自定义权限数据源，提供所有URL资源与对应角色权限的映射集合
 * 重写并实现了基于数据库的权限数据源
 *
 * @author tuess
 * @version 1.0
 **/
@Service
@Slf4j
public class AccessSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {


    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        Principal principal = ((FilterInvocation) o).getHttpRequest().getUserPrincipal();
        Authentication authentication = (Authentication) principal;

        // 防止有一些不登录即可访问的资源在此NPE
        return authentication == null ? null : SecurityConfig.createList(String.valueOf(authentication.getAuthorities()));
    }

    /**
     * 用于被AbstractSecurityInterceptor调用，返回所有的 Collection<ConfigAttribute> ，以筛选出不符合要求的attribute
     */

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<>();
    }


    /**
     * 用于被AbstractSecurityInterceptor调用，验证指定的安全对象类型是否被MetadataSource支持
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
