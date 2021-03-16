// package com.kdyzm.spring.security.oauth.study.resource.server.filter;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.util.AntPathMatcher;
//
// import javax.servlet.*;
// import javax.servlet.annotation.WebFilter;
// import javax.servlet.http.HttpServletRequest;
// import java.io.IOException;
// import java.io.PrintWriter;
// import java.util.Collection;
//
// /**
//  * 用来进行权限校验的一个过滤器
//  *
//  * @author tuess
//  * @version 1.0
//  **/
// @Slf4j
// // @Order(1)
// @WebFilter(filterName = "myFilter1", urlPatterns = {"/*"})
// public class PermissionFilter implements Filter {
//
//     private final AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//     @Override
//     public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//         HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//         boolean hasPermission = false;
//         log.debug("-------url:{}", httpServletRequest.getRequestURI());
//         // 读取用户所拥有的权限菜单
//         for (GrantedAuthority authority : authorities) {
//             log.debug("权限为：{}", authority.getAuthority());
//             if (antPathMatcher.match(authority.getAuthority(), httpServletRequest.getRequestURI())) {
//                 hasPermission = true;
//                 break;
//             }
//         }
//         if (hasPermission) {
//             filterChain.doFilter(servletRequest, servletResponse);
//         } else {
//             PrintWriter writer = null;
//             servletResponse.setCharacterEncoding("UTF-8");
//             servletResponse.setContentType("text/html; charset=utf-8");
//             try {
//                 writer = servletResponse.getWriter();
//                 writer.print("{\n" +
//                         "  \"code\":403,\n" +
//                         "  \"msg\": \"没有权限\"\n" +
//                         "}");
//
//             } catch (IOException e) {
//                 log.error("response error", e);
//             } finally {
//                 if (writer != null) {
//                     writer.close();
//                 }
//             }
//         }
//     }
// }
