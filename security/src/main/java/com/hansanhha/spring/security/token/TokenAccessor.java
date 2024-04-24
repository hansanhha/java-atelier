package com.hansanhha.spring.security.token;

public interface TokenAccessor<T, R> {

    TokenType getTokenType();

    T get();

    default R getAccessId() {
        return null;
    }
}
