package com.kdyzm.spring.security.oauth.study.resource.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import java.io.IOException;

/**
 * 资源访问过滤器
 *
 * @author tuess
 * @version 1.0
 **/
@Service
public class CustomizeAbstractSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    @Autowired
    private AccessSecurityMetadataSource securityMetadataSource;

    @Autowired
    public void setMyAccessDecisionManager(AccessDecisionManagerImpl accessDecisionManager) {
        super.setAccessDecisionManager(accessDecisionManager);
    }


    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        //fi里面有一个被拦截的url
        //里面调用AccessSecurityMetadataSource的getAttributes(Object object)这个方法获取fi对应的所有权限
        //再调用AccessDecisionManagerImpl的decide方法来校验用户的权限是否足够
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            //执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }
}
