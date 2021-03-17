package com.kdyzm.spring.security.oauth.study.resource.server.controller;

import com.kdyzm.spring.security.oauth.study.resource.server.entity.JwtTokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kdyzm
 */

@Slf4j
@RestController
@RequestMapping("/test")
public class OrderController {


    @GetMapping("/r1")
    // @PreAuthorize("hasAnyAuthority('/r1')")
    public String r1() {
        return "访问资源r1";
    }

    @GetMapping("/r2")
    // @PreAuthorize("hasAnyAuthority('/r2')")
    public String r2() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtTokenInfo jwtTokenInfo = (JwtTokenInfo) authentication.getPrincipal();
        return "访问资源r2" + jwtTokenInfo.getUser_info().getBranch();
    }

    @GetMapping("/r3")
    // @PreAuthorize("hasAnyAuthority('/r3')")
    public String r3() {
        return "a";
    }

}
