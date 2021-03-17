package com.kdyzm.spring.security.auth.center.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 用户信息扩展类,继承security的User实体类
 * 对于其中默认重写了的getUsername()和getPassword()方法，要调用父类的方法
 *
 * @author tuess
 * @version 1.0
 **/
public class ExpandUser extends User {
    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private String mobile;
    private String branch;

    public ExpandUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
