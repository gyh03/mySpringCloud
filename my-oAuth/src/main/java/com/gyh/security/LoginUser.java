package com.gyh.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Collection;

public class LoginUser extends User implements Principal{

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LoginUser(String username, String userId, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId=userId;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
