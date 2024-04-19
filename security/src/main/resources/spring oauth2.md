## Spring Security Authorization Grant support

Authorization Code

Refresh Token

Client Credentials

Resource Owner Password Credentials

JWT Bearer

client authentication support
- JWT Bearer

## Spring Security OAuth 2.0 workflow

- user request(client application login thorough OAuth2 provider)
  - 카카오 로그인, 네이버 로그인, 구글 로그인 등
- client server redirect to authorization server login page
  - OAuth2AuthorizationRequestRedirectFilter
  - OAuth2AuthorizationRequestResolver -> OAuth2AuthorizeRequest 생성
  - ClientRegistration, ClientRegistrationRepository
- user authentication to authorization server 
- user grant access to client application 
- authorization server redirect to client server with authorization code 
  - user redirected back to client application
- client server request access token to authorization server with authorization code
  - OAuth2LoginAuthenticationFilter (OAuth2AuthenticationToken)
  - OAuth2LoginAuthenticationProvider, OAuth2AuthorizationCodeAuthenticationProvider
  - OAuth2AuthorizedClientProvider, OAuth2AccessTokenResponseClient
  - OAuth2AuthorizedClientService, OAuth2AuthorizedClient
- authorization server validate authorization code, response access token (optional refresh token)
- request user info to authorization server with access token
  - OAuth2UserService, OidcUserService
  - OAuth2UserRequest
- authorization server validate access token, response user info
- verify client application first login
  - if first, create user account
- request access to resource server with access token 
  - OAuth2AuthorizedClientManager
  - OAuth2AuthorizedClient
- resource server validate access token, response resource data
  - OAuth2AuthenticationProcessingFilter
  - OAuth2AuthorizedClientManager, OAuth2AuthorizedClientService, OAuth2AuthorizedClientRepository
  - JwtDecoder or OpaqueTokenIntrospector

## Spring Security OAuth2 Client

Filter

OAuth2AuthorizationRequestRedirectFilter
- 사용자가 인증을 시작할 때 요청을 가로채고 OAuth 2.0 Authorization Server로 리다이렉션하는 스프링 시큐리티 FilterChain의 Filter
- 협력 객체
  - OAuth2AuthorizationRequestResolver : `/oauth2/authorization/{registrationId}`로 요청이 들어오면 OAuth2AuthorizeRequest를 생성하고 end-user를 인증 페이지로 리다이렉트함
  - OAuth2AuthorizeRequest : OAuth 2.0 인증 프로세스를 시작할 때 사용하는 객체, 인증을 처리하는 데 필요한 정보(redirect uri, client id, scope 등)
  - AuthorizationRequestRepository :  인증 요청 초기화 시간부터 인증 응답 수신 시간까지 OAuth2AuthorizationRequest를 저장하는 역할
    - 구현체 : (default) HttpSessionOAuth2AuthorizationRequestRepository - HttpSession에 OAuth2AuthorizationRequest 저장
  - ClientRegistration : OAuth 2.0 Provider, OpenId Connect 1.0에 등록된 Client 정보(client id, client secret, redirect uri 등)를 저장하는 객체
  - ClientRegistrationRepository : ClientRegistration 객체 저장소
    - 구현체 : (default) InMemoryClientRegistrationRepository
  - ContextAttributesMapper
    - OAuth2AuthorizeRequest에 attribute를 매핑하는 용도
    - DefaultOAuth2AuthorizedClientManager는 Function<OAuth2AuthrozaionRequest, Map<String, Object>> 타입의 ContextAttributesMapper를 사용

OAuth2LoginAuthenticationFilter
- 사용자가 authorization server에서 인증을 완료한 후 리다이렉트된 요청 처리
- authorization grant를 받고 token endpoint로 access token 요청, OAuth2AuthenticationToken 반환, OAuth2AuthorizedClient 생성 및 OAuth2AuthorizedClientRepository에 저장
- 협력 객체
  - ClientRegistrationRepository 
  - OAuth2LoginAuthenticationProvider : 사용자 정보 로드, OAuth2LoginAuthenticationToken을 처리하는 역할, 내부적으로 OAuth2AuthorizationCodeAuthenticationProvider 사용하여 access token 요청 
    - OAuth2UserService, OidcUserService(OpenId Connect)
      - OAuth2 Provider로부터 인증된 사용자 정보를 가져오는 역할
    - OAuth2UserRequest
      - 인증된 사용자 정보(이메일 등)를 가져오기 위한 요청 객체
  - OAuth2AuthorizationCodeAuthenticationProvider : authorization code 플로우를 사용하는 경우, authorization code를 받아 access token을 요청 및 OAuth2AuthorizationCodeAuthenticationToken 처리 역할
  - OAuth2AccessTokenResponseClient : 실제로 authorization code를 access token으로 교환하는 역할(OAuth2AccessTokenResponse 반환)
    - 구현체 : (default) DefaultAuthorizationCodeTokenResponseClient - authorization code 사용, RestOperations 인스턴스 사용
  - OAuth2AuthorizedClientProvider : 스프링 시큐리티에서 지원하는 인증 플로우 구현체 제공
    - 구현체 : AuthorizationCodeOAuth2AuthorizedClientProvider 등
    - OAuth2AuthorizedClientProviderBuilder로 위임 기반 composite 구성 가능
  - OAuth2AuthorizedClientService : OAuth2AuthorizedClient 인스턴스 저장, 검색하는 역할
    - 구현체 : (default) InMemoryOAuth2AuthorizedClientService, JdbcOAuth2AuthorizedClientService
  - OAuth2AuthorizedClientRepository : 웹 요청 간 OAuth2AuthorizedClient를 저장, 검색(persisting)하는 역할
  - OAuth2AuthorizedClient : OAuth 2.0 인증 프로세스를 통해 얻은 Client 정보(access token, refresh token, client id 등)를 나타냄


OAuth2ClientContextFilter (Spring Security 5.1 이전)
- 사용자가 authorization server에서 인증을 마친 뒤 service application으로의 리다이렉션 처리
- authorization grant type을 access token으로 요청하는 과정 관리
- 협력 객체
  - OAuth2ClientContext : 사용자의 현재 인증 상태(access, refresh token) 저장 역할
  - OAuth2AccessTokenResponseClient : authorization grant를 통해 access token (재)발급 요청 역할
  - AccessTokenRequest : access token을 요청할 때 사용하는 객체(authrozation code, redirect uri 등)

---



### OAuth2 Client Configuration

## OAuth2 Resource Server

Filter

OAuth2AuthenticationProcessingFilter
- OAuth 2.0 protected resource 요청 처리
- access token 검증 및 해당 토큰과 인증 정보를 SecurityContext에 저장
- 협력 객체
  - OAuth2AuthenticationProcessingFilter
  - OAuth2AuthorizedClientManager : OAuth2AuthorizedClient 생성, 로딩, 저장하는 역할
    - 구현체 : (default) DefaultOAuth2AuthorizedClientManager, AuthorizedClientServiceOAuth2AuthorizedClientManager
  - OAuth2AuthorizationSuccessHandler
    - OAuth 2.0 Client 인증 성공 시 호출되는 객체
    - OAuth2AuthorizedClientRepository에 OAuth2AuthorizedClient 저장
  - OAuth2AuthorizationFailureHandler
    - RemoveAuthorizedClientOAuth2AuthorizationFailureHandler
      - 재인증 실패 시(refresh token 만료 등) OAuth2AuthorizedClientRepository에 저장된 OAuth2AuthorizedClient 제거

---

ResourceServerTokenServices

OAuth2ResourceServerConfigurer

JwtDecoder

OpaqueTokenIntrospector

JwtAuthenticationConverter

BearerTokenAuthenticationFilter

Spring Security 는 OAuth2 Access Token을 위한 두 개의 Bearer 타입 지원
- JWT : JwtDecoder bean
- Opaque token : OpaqueTokenIntrospector

### OAuth2 Resource Server Configuration

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
