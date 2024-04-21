package com.hansanhha.spring.oauth2.user;

import lombok.Getter;

@Getter
public enum Authority {

    READ("MEMBER_READ"),
    WRITE("MEMBER_WRITE"),
    DELETE("MEMBER_DELETE");

    private final String authority;

    Authority(String authority) {
        this.authority = authority;
    }
}
