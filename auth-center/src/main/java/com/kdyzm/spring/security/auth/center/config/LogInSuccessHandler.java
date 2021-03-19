package com.kdyzm.spring.security.auth.center.config;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kdyzm.spring.security.auth.center.entity.TUser;
import com.kdyzm.spring.security.auth.center.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 登录成功处理器
 *
 * @author tuess
 * @version 1.0
 **/
@Component
public class LogInSuccessHandler implements ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    UserMapper userMapper;


    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        userMapper.update(null, new LambdaUpdateWrapper<TUser>().set(TUser::getFullname, LocalDateTime.now().toString()).eq(TUser::getUsername, authenticationSuccessEvent.getAuthentication().getName()));
        System.out.println("11111111111111111111111111111");
    }
}
