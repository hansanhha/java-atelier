package com.hansanhha.spring.security.token;

import com.hansanhha.spring.security.token.jwt.access.TokenAccessor;

public interface TokenService<Spec> {

    TokenAccessor<?> generateAccessToken(Spec spec);

    TokenAccessor<?> generateRefreshToken(Spec spec);

    TokenAccessor<?> resolveToken(String value);

    void removeToken(TokenAccessor<?> accessor);
}
