package com.hansanhha.spring.security.user;

import lombok.Getter;

import java.util.Set;

@Getter
public enum Role {

    USER("ROLE_USER",
            Set.of(Authority.READ, Authority.WRITE, Authority.DELETE)
    );

    private String roleName;

    private Set<Authority> authorities;

    Role(String roleName, Set<Authority> authorities) {
        this.roleName = roleName;
        this.authorities = authorities;
    }
}
