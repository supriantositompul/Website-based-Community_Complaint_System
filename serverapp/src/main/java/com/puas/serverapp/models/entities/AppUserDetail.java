package com.puas.serverapp.models.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppUserDetail implements UserDetails{
    private User user;

    private static final Logger logger = LoggerFactory.getLogger(AppUserDetail.class);

    @SuppressWarnings("unchecked")
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        @SuppressWarnings("rawtypes")
        List<GrantedAuthority> authorities = new ArrayList();
        user.getRoles().stream().forEach(role -> {
            String roles = "ROLE_" + role.getName().toUpperCase();
            authorities.add(new SimpleGrantedAuthority(roles));
            logger.info("Added role: ROLE_" + role.getName().toUpperCase());
            role.getPrivileges().forEach(privilege -> {
                String privileges = privilege.getName().toUpperCase();
                authorities.add(new SimpleGrantedAuthority(privileges));
                logger.info("Added privilege: " + privilege.getName().toUpperCase());
            });
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getIsEnabled();
    }
}
