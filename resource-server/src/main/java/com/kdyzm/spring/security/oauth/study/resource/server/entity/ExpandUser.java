package com.kdyzm.spring.security.oauth.study.resource.server.entity;

import lombok.Data;

/**
 * 用户信息扩展类,继承security的User实体类
 * 对于其中默认重写了的getUsername()和getPassword()方法，要调用父类的方法
 *
 * @author tuess
 * @version 1.0
 **/
@Data
public class ExpandUser {

    private Integer id;

    private String username;

    private String password;

    private String fullname;

    private String mobile;

    private String branch;


}
