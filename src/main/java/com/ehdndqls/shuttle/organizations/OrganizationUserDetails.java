package com.ehdndqls.shuttle.organizations;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class OrganizationUserDetails implements UserDetails {

    private final Organizations organization;

    public OrganizationUserDetails(Organizations organization) {
        this.organization = organization;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 설정
        return Collections.emptyList();
    }

    public Long getId() { return organization.getOrganizationId(); }

    @Override
    public String getPassword() {
        return organization.getOrganizationPassword();
    }

    @Override
    public String getUsername() {
        return organization.getOrganizationName(); // 여기서 username으로 사용
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
        return true;
    }
}
