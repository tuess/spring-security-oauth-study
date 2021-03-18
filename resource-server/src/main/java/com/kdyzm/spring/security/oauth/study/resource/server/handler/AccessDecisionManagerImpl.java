package com.kdyzm.spring.security.oauth.study.resource.server.handler;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.kdyzm.spring.security.oauth.study.resource.server.entity.JwtTokenInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
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
    @SneakyThrows
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        HttpServletRequest request = ((FilterInvocation) o).getHttpRequest();
        log.info("[用户允许请求的地址]: {}", authentication.getAuthorities());

        String resourceRole = request.getRequestURI();
        log.info("[资源地址]: {}", resourceRole);

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();


        if (StringUtils.isEmpty(details.getTokenValue())) {
            log.info("未找到token信息");
            return;
        }

        String[] base64Token = details.getTokenValue().split("\\.");
        String token = Base64.decodeStr(base64Token[1]);
        JwtTokenInfo jwtTokenInfo = JSONObject.parseObject(token, JwtTokenInfo.class);

        log.info("用户部门：{}", jwtTokenInfo.getUser_info().getBranch());

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(jwtTokenInfo, null, authentication.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //将authenticationToken填充到安全上下文便于以后使用
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        // 对比权限
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
