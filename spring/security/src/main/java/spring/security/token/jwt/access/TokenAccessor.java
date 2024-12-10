package spring.security.token.jwt.access;

import spring.security.token.TokenType;

public interface TokenAccessor<T> {

    TokenType getTokenType();

    T get();

    default String getAccessId() {
        return null;
    }
}
