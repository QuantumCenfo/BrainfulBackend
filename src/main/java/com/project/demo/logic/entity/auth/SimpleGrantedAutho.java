package com.project.demo.logic.entity.auth;

import org.springframework.security.core.GrantedAuthority;

public class SimpleGrantedAutho implements GrantedAuthority {

    private String authority;

    public SimpleGrantedAutho() {
    }

    public SimpleGrantedAutho(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
