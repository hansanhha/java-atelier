package com.hansanhha.spring.security.token.jwt.repository;

import com.hansanhha.spring.security.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtAccessTokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByAccessId(String accessId);

    Optional<Token> findByTokenValue(String tokenValue);
}
