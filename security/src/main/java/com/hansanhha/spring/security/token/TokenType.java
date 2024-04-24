package com.hansanhha.spring.security.token;

import lombok.Getter;

@Getter
public enum TokenType {

    ACCESS_TOKEN("access_token"),
    REFRESH_TOKEN("refresh_token");

    private final String type;

    TokenType(String type) {
        this.type = type;
    }
}
