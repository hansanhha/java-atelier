package com.hansanhha.spring.security.token.jwt.access;

import com.hansanhha.spring.security.token.TokenType;

public interface TokenAccessor<T> {

    TokenType getTokenType();

    T get();

    default String getAccessId() {
        return null;
    }
}
