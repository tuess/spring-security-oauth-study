package com.kdyzm.spring.security.auth.center.config;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录，可以在这里使token过期
 *
 * @author tuess
 * @version 1.0
 **/
@Component
public class LogOutHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String loginOutUrl = httpServletRequest.getRequestURI();

        if (!"".equals(loginOutUrl)) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(("退出成功")));
        } else {
            httpServletResponse.sendRedirect(loginOutUrl);
        }

    }
}
