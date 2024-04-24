package com.hansanhha.spring.security.controller;

import com.hansanhha.spring.security.token.jwt.JwtTokenService;
import com.hansanhha.spring.security.token.TokenAccessor;
import com.hansanhha.spring.security.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OAuth2UserController {

    private final UserRepository userRepository;
    private final JwtTokenService tokenService;

    @PostMapping("/api/user")
    public Map<String, String> user(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String accessToken = authorization.substring("Bearer ".length()).trim();

        log.info(this.getClass().getSimpleName());
        log.info("- requested user info");
        log.info("- accessId: {}", accessToken);

        TokenAccessor<Jwt, String> accessTokenAccessor = tokenService.loadTokenByAccessTokenValue(accessToken);
        String email = userRepository.findByEmail(accessTokenAccessor.get().getSubject()).orElseThrow().getEmail();

        log.info("- return user info");
        log.info("- email: {}", email);

        return Map.of("email", email);
    }
}
