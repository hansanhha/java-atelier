package com.hansanhha.spring.security.token;

public interface TokenService<T, TokenId, R> {

    R loadTokenByAccessId(TokenType type, TokenId accessId);

    R generateAccessToken(T t);

    R generateRefreshToken(T t);

    void removeToken(TokenType type, TokenId tokenId);
}
