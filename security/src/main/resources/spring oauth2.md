## OAuth2 Resource Server

OSpring Security 는OAuth2 Access Token을 위한 두 개의 Bearer 타입 지원
- JWT : JwtDecoder bean
- Opaque token : OpaqueTokenIntrosector

JWT 발행자 지정

```
 spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://my-auth-server.com
```

아래와 동일한 설정

```
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize
				.anyRequest().authenticated()
			)
			.oauth2ResourceServer((oauth2) -> oauth2
				.jwt(Customizer.withDefaults())
			);
		return http.build();
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		return JwtDecoders.fromIssuerLocation("https://my-auth-server.com");
	}

}
```

## OAuth2 Client


