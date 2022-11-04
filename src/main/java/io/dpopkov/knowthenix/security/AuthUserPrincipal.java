package io.dpopkov.knowthenix.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class AuthUserPrincipal implements UserDetails {
    private final String username;
    private final String password;
    private final Collection<GrantedAuthority> authorities;
    private final boolean notLocked;
    private final boolean isEnabled;

    public AuthUserPrincipal(String username, String password, Collection<String> authorities) {
        this(username, password, authorities, false, false);
    }

    public AuthUserPrincipal(String username, String password, Collection<String> authorities,
                             boolean notLocked, boolean isEnabled) {
        this.authorities = authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        this.password = password;
        this.username = username;
        this.notLocked = notLocked;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return notLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
