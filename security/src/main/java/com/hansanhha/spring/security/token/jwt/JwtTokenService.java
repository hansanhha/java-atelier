package com.hansanhha.spring.security.token.jwt;

import com.hansanhha.spring.security.token.Token;
import com.hansanhha.spring.security.token.TokenAccessor;
import com.hansanhha.spring.security.token.TokenService;
import com.hansanhha.spring.security.token.TokenType;
import com.hansanhha.spring.security.token.jwt.repository.JwtAccessTokenRepository;
import com.hansanhha.spring.security.token.jwt.repository.JwtRefreshTokenRepository;
import com.hansanhha.spring.security.user.User;
import com.hansanhha.spring.security.user.repository.UserRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenService<JwtTokenProvider.JwtGenerateSpec, String, TokenAccessor<Jwt, String>> {

    private final JwtAccessTokenRepository accessTokenRepository;
    private final JwtRefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${service.security.secret-key}")
    private String plainSecretKey;
    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;

    @PostConstruct
    public void init(){
        byte[] plainSecretKeyBytes = plainSecretKey.getBytes(StandardCharsets.UTF_8);

        SecretKeySpec secretKeySpec = new SecretKeySpec(plainSecretKeyBytes, MacAlgorithm.HS256.getName());

        jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKeySpec));
        jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Override
    public TokenAccessor<Jwt, String> generateAccessToken(JwtTokenProvider.JwtGenerateSpec spec) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(spec.getAccessTokenId())
                .issuer(spec.getIssuer())
                .issuedAt(spec.getIssueTime())
                .expiresAt(spec.getAccessTokenExpiredTime())
                .subject(spec.getSubject())
                .build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(spec.getJwsHeader(), claims);
        Jwt jwt = jwtEncoder.encode(parameters);

        User user = userRepository.findByEmail(spec.getPrincipalName()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        saveAccessToken(jwt, user);

        return JwtAccessTokenAccessor.from(jwt);
    }

    @Override
    public TokenAccessor<Jwt, String> generateRefreshToken(JwtTokenProvider.JwtGenerateSpec spec) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(spec.getRefreshTokenId())
                .issuer(spec.getIssuer())
                .issuedAt(spec.getIssueTime())
                .expiresAt(spec.getRefreshTokenExpiredTime())
                .subject(spec.getSubject())
                .build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(spec.getJwsHeader(), claims);
        Jwt jwt = jwtEncoder.encode(parameters);

        User user = userRepository.findByEmail(spec.getPrincipalName()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        saveRefreshToken(jwt, user);

        return JwtRefreshTokenAccessor.from(jwt);
    }

    public TokenAccessor<Jwt, String> loadTokenByAccessTokenValue(String value) {
        Token token = accessTokenRepository.findByTokenValue(value).orElseThrow(() -> new IllegalArgumentException("Token not found"));
        Jwt jwt = jwtDecoder.decode(token.getTokenValue());
        return JwtAccessTokenAccessor.from(jwt);
    }

    @Override
    public TokenAccessor<Jwt, String> loadTokenByAccessId(TokenType type, String accessId) {
        return switch (type) {
            case ACCESS_TOKEN -> {
                Token token = accessTokenRepository.findByAccessId(accessId).orElseThrow(() -> new IllegalArgumentException("Token not found"));
                Jwt jwt = jwtDecoder.decode(token.getTokenValue());
                yield JwtAccessTokenAccessor.from(jwt);
            }
            case REFRESH_TOKEN -> {
                Token token = refreshTokenRepository.findByAccessId(accessId).orElseThrow(() -> new IllegalArgumentException("Token not found"));
                Jwt jwt = jwtDecoder.decode(token.getTokenValue());
                yield JwtRefreshTokenAccessor.from(jwt);
            }
        };
    }

    @Override
    public void removeToken(TokenType type, String id) {
//        switch (type) {
//            case ACCESS_TOKEN:
//                accessTokenRepository.deleteById(id);
//                break;
//            case REFRESH_TOKEN:
//                refreshTokenRepository.deleteById(id);
//                break;
//        }
    }

    public void revokeRefreshTokens(String userId) {
        User user = userRepository.findByEmail(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Token> tokens = refreshTokenRepository.findAllByUser(user);

        tokens.forEach(token -> token.setUsed(false));
    }

    private void saveAccessToken(Jwt jwt, User user) {
        Token token = Token.builder()
                .accessId(jwt.getClaim(JwtClaimNames.JTI))
                .tokenValue(jwt.getTokenValue())
                .issuedAt(jwt.getIssuedAt())
                .expiredAt(jwt.getExpiresAt())
                .user(user)
                .build();

        accessTokenRepository.save(token);
    }

    private void saveRefreshToken(Jwt jwt, User user) {
        Token token = Token.builder()
                .accessId(jwt.getClaim(JwtClaimNames.JTI))
                .tokenValue(jwt.getTokenValue())
                .issuedAt(jwt.getIssuedAt())
                .expiredAt(jwt.getExpiresAt())
                .user(user)
                .build();

        refreshTokenRepository.save(token);
    }
}
