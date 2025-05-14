package com.example.bookstore.security;

import com.example.bookstore.persistance.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final Long userId;
    private final String username;
    private final String password;
    private final boolean isEnabled;
    private final List<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user, List<? extends GrantedAuthority> authorities)
    {
        this.userId = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.isEnabled = user.isEnabled();
        this.authorities = authorities;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
