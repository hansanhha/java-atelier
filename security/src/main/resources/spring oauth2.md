## Spring Security OAuth 2.0 workflow

- user request(service application login)
  - 카카오 로그인, 네이버 로그인, 구글 로그인 등
- service server redirect to authorization server login page
  - OAuth2AuthorizationRequestRedirectFilter
  - OAuth2AuthorizationRequestResolver, OAuth2AuthorizeRequest
- user authentication to authorization server 
- user grant access to service application 
- authorization server redirect to service server with authorization code 
  - user redirected back to client application
- service server request access token to authorization server with authorization code 
  - OAuth2LoginAuthenticationProvider
  - OAuth2AccessTokenResponseClient(DefaultAuthorizationCodeTokenResponseClient)
- authorization server validate authorization code, response access token (optional refresh token)
- service server request access to resource server with access token 
  - OAuth2AuthorizedClientManager
  - OAuth2AuthorizedClient
- resource server validate access token, response resource data
  - OAuth2ResourceServerConfigurer
  - JwtDecoder or OpaqueTokenIntrospector


## OAuth2 Client

ClientRegistration
- OAuth2, OpenID Connect 1.0 Provider에 등록된 Client 표현 객체
- client id, client secret, authorization grant type, redirect uri 등 정보

ClientRegistrationRepository
- ClientRegistration 객체 저장소
- 구현체
  - (Default) InMemoryClientRegistrationRepository
  - SupplierClientRegistrationRepository

OAuth2AuthorizedClient
- ClientRegistration과 Resource Owner에게 OAuth2AccessToken을 연결시키기 위한 목적
    - (optional) OAuth2RefreshToken
- end-user(Resource Owner)가 권한을 client에게 부여한 경우 Authorized Client가 됨

OAuth2AuthorizedClientRepository
- 웹 요청 간 OAuth2AuthorizedClient를 persisting하는 역할

OAuth2AuthorizedClientService
- 애플리케이션 수준에서 OAuth2AuthorizedClient를 관리하는 역할
- 구현체
  - (Default) InMemoryOAuth2AuthorizedClientService
  - JdbcOAuth2AuthorizedClientService

OAuth2AuthorizedClientManager
- OAuth2AuthorizedClient를 전반적으로 관리하는 역할
  - OAuth2AuthorizedClientProvider를 통한 OAuth 2.0 인증(또는 재인증)
  - OAuth2AuthorizedClientRepository 또는 OAuth2AuthorizedClientService를 통한 OAuth2AuthorizedClient persistence 위임 
  - OAuth 2.0 Cilent 인증 성공 시 OAuth2AuthorizaionSuccessHandler를 통한 후속 처리 위임
  - OAuth 2.0 Client 인증 실패 시 OAuth2AuthorizationFailureHandler를 통한 후속 처리 위임
- 구현체
  - (default) DefaultOAuth2AuthorizedClientManager
  - AuthorizedClientServiceOAuth2AuthorizedClientManager

OAuth2AuthorizedClientProvider
- OAuth 2.0 Client 인증 전략 구현 역할
  - 위임 기반 composite를 통해 여러 grant type을 지원함
  - authorization grant type - authorization_code, client_credentials 등
- 구현체
  - AuthorizationCodeOAuth2AuthorizedClientProvider - Authorization Code Grant
- OAuth2AuthorizedClientProviderBuilder로 composite 구성 가능

OAuth2AuthorizationSuccessHandler
- OAuth 2.0 Client 인증 성공 시 호출되는 객체
- OAuth2AuthorizedClientRepository에 OAuth2AuthorizedClient 저장

OAuth2AuthorizationFailureHandler
- RemoveAuthorizedClientOAuth2AuthorizationFailureHandler
  - 재인증 실패 시(refresh token 만료 등) OAuth2AuthorizedClientRepository에 저장된 OAuth2AuthorizedClient 제거

ContextAttributesMapper
- OAuth2AuthorizeRequest에 attribute를 매핑하는 용도
- DefaultOAUth2AuthorizedClientManager는 Function<OAuth2AuthrozaionRequest, Map<String, Object>> 타입의 ContextAttributesMapper를 사용

---

OAuth2ClientContextFilter

AuthorizationCodeAccessTokenProvider

OAuth2AuthorizeRequest
- OAuth 2.0 인증 프로세스를 시작할 때 사용되는 객체
- 인증을 처리하는 데 필요한 정보(redirect uri, client id, scope 등)

OAuth2AuthorizationRequestRedirectFilter
- 사용자가 인증을 시작할 때 요청을 가로채고 OAuth 2.0 Authorization Server로 리다이렉션하는 스프링 시큐리티 FilterChain의 Filter
- `/oauth2/authorization/{registrationId}`로 요청이 들어오면 OAuth2AuthorizationRequestResolver를 통해 OAuth2AuthorizeRequest를 생성하고 end-user의 user-agent를 인증 페이지로 리다이렉트함

OAuth2AuthorizationRequestResolver
- 요청의 registrationId에 매칭되는 ClientRegistration을 통해 OAuth2AuthorizeRequest를 생성
- OAuth2AuthorizeRequest 상세 사항 결정(redirect uri, client id, scope 등)

OAuth2AccessTokenResponseClient
- AccessToken 요청을 처리하는 역할
- DefaultAuthorizationCodeTokenResponseClient

RestOperations

## OAuth2 Resource Server

ResourceServerTokenServices

OAuth2ResourceServerConfigurer

JwtDecoder

OpaqueTokenIntrospector

JwtAuthenticationConverter

BearerTokenAuthenticationFilter

Spring Security 는 OAuth2 Access Token을 위한 두 개의 Bearer 타입 지원
- JWT : JwtDecoder bean
- Opaque token : OpaqueTokenIntrospector

JWT 발행자 지정

```
 spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://my-auth-server.com
```

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
