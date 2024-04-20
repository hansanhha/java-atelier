## Spring Security OAuth 2.0 Client 

### Authorization Grant Support

Authorization Code

Refresh Token

Client Credentials

Resource Owner Password Credentials

JWT Bearer

#### Client Authentication Support

JWT Bearer

## Client Workflow

Spring Security OAuth 2.0 동작 흐름은 Spring Security Architecture를 기반으로 함

Filter -> AuthenticationManager -> AuthenticationProvider -> UserDetailsService

SecurityContextHolder(SecurityContext, Repository, Authentication)

1. user request(client application login thorough OAuth2 provider)
    - 카카오 로그인, 네이버 로그인, 구글 로그인 등
2. client server redirect to authorization server login page 
    - OAuth2AuthorizationRequestRedirectFilter
    - OAuth2AuthorizationRequestResolver -> OAuth2AuthorizeRequest 생성
    - ClientRegistration, ClientRegistrationRepository
3. user authentication to authorization server 
4. user grant access to client application 
5. authorization server redirect to client server with authorization code 
    - user redirected back to client application
6. client server request access token to authorization server with authorization code
    - OAuth2LoginAuthenticationFilter (OAuth2AuthenticationToken)
    - OAuth2LoginAuthenticationProvider - OAuth2AuthorizationCodeAuthenticationProvider - OAuth2AccessTokenResponseClient
    - OAuth2AuthorizedClientProvider, OAuth2AuthorizedClientService, OAuth2AuthorizedClient
7. authorization server validate authorization code, response access token (optional refresh token)
8. request user info to authorization server with access token
    - OAuth2UserService, OidcUserService
    - OAuth2UserRequest
9. authorization server validate access token, response user info
10. verify first login user
    - if first, create user account
11. request access to resource server with access token 
    - OAuth2AuthorizedClientManager
    - OAuth2AuthorizedClient
12. resource server validate access token, response resource data
    - OAuth2AuthenticationProcessingFilter
    - OAuth2AuthorizedClientManager, OAuth2AuthorizedClientService, OAuth2AuthorizedClientRepository
    - JwtDecoder or OpaqueTokenIntrospector

## Client Filter

OAuth2AuthorizationRequestRedirectFilter
- 사용자가 OAuth 2.0 로그인을 시작하면 요청을 Authorization Server로 리다이렉트하는 필터
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

DefaultLoginPageGeneratingFilter
- 애플리케이션 로그인 페이지를 자동으로 생성하는 필터
- OAuth2 로그인(oauth2Login())이 구성된 경우 OAuth2 Provider 링크가 포함된 로그인 페이지 생성 가능
- 사용하지 않으려면 대신하는 로그인 페이지 지정
  - `/login/oauth2/code/*` 경로로 리다이렉션 필요

OAuth2LoginAuthenticationFilter
- 사용자가 인증을 완료한 뒤 인증 서버에서 보낸 리다이렉트 수신, access token 요청 후 발급된 access, refresh token과 clientRegistration 정보 등을 SecurityContext에 저장
- (detail) authorization grant를 받고 token endpoint로 access token 요청, OAuth2AuthenticationToken 반환, OAuth2AuthorizedClient 생성 및 OAuth2AuthorizedClientRepository에 저장
- 협력 객체
  - ClientRegistrationRepository 
  - OAuth2LoginAuthenticationProvider : access token 요청, 사용자 정보 로드, OAuth2LoginAuthenticationToken 반환, 내부적으로 OAuth2AuthorizationCodeAuthenticationProvider, OAuth2UserService(OidcUserService) 사용 
    - OAuth2UserService, OidcUserService(OpenId Connect) : OAuth2 Provider로부터 인증된 사용자 정보를 가져오는 역할
    - OAuth2UserRequest : 인증된 사용자 정보(이메일 등)를 가져오기 위한 요청 객체
  - OAuth2AuthorizationCodeAuthenticationProvider : authorization code를 통해 access token 요청, OAuth2AuthorizationCodeAuthenticationToken 반환
  - OAuth2AccessTokenResponseClient : 실제로 authorization code를 access token으로 교환하는 역할(OAuth2AccessTokenResponse 반환)
      - 구현체 : (default) DefaultAuthorizationCodeTokenResponseClient - authorization code 사용, RestOperations 인스턴스 사용
  - OAuth2AuthorizedManager : 포괄적인 OAuth2AuthorizedClient 관리 역할
  - OAuth2AuthorizedClientService : 애플리케이션 수준에서 OAuth2AuthorizedClient 인스턴스 저장, 검색하는 역할
      - 구현체 : (default) InMemoryOAuth2AuthorizedClientService, JdbcOAuth2AuthorizedClientService
  - OAuth2AuthorizedClientProvider : 스프링 시큐리티 OAuth 2.0에서 지원하는 인증 플로우 구현체 제공
      - 구현체 : AuthorizationCodeOAuth2AuthorizedClientProvider, RefreshTokenOAuth2AuthorizedClientProvider 등
      - OAuth2AuthorizedClientProviderBuilder로 위임 기반 composite 구성 가능
  - OAuth2AuthorizedClientRepository : 웹 요청 간 OAuth2AuthorizedClient를 저장, 검색(persisting)하는 역할
  - OAuth2AuthorizedClient : OAuth 2.0 인증 프로세스를 통과한 Client 정보(clientRegistration, access token, refresh token, end-user principal)
  - OAuth2AuthorizationSuccessHandler : 인증 성공 시 후처리
  - OAuth2AuthorizationFailureHandler : 인증 실패 시 후처리

OAuth2ClientContextFilter (Spring Security 5.1 이전)
- 사용자가 authorization server에서 인증을 마친 뒤 service application으로의 리다이렉션 처리
- authorization grant type을 access token으로 요청하는 과정 관리
- 협력 객체
  - OAuth2ClientContext : 사용자의 현재 인증 상태(access, refresh token) 저장 역할
  - OAuth2AccessTokenResponseClient : authorization grant를 통해 access token (재)발급 요청 역할
  - AccessTokenRequest : access token을 요청할 때 사용하는 객체(authrozation code, redirect uri 등)

### Client Configuration

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2Config {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2Client(oauth2 -> oauth2
                        .clientRegistrationRepository(...)
                        .authorizedClientRepository(...)
                        .authroizedClientService(...)
                        .authorizationCodeGrant(codeGrant -> codeGrant
                                .authorizationRequestRepository(...)
                                .authorizationRequestResolver(...)
                                .accessTokenResponseClient(...)
                        )
                )
                .oauth2Login(oauth2login -> oauth2login
                        .loginPage("/oauth2/authorization/custom-login-page")
                )
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler handler = new SimpleUrlLogoutSuccessHandler();
        handler.setUseReferer(true);
        return handler;
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authoriationCode()
                        .refreshToken()
                        .clientCredentials()
                        .password()
                        .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
```

client registration, provider에 대한 상세 정보는 프로퍼티 파일에 명시

OAuth2ClientConfigurer (oauth2Client())
- OAuth 2.0 클라이언트 기능 설정
- 관련 객체
    - AuthorizationCodeGrantConfigurer  


OAuth2LoginConfigurer (oauth2Login())
- 애플리케이션 전체의 OAuth 2.0 로그인 관련 설정(login page, redirect uri, authorization endpoint 등)
- 프로퍼티 파일에 설정한 OAuth2 Provider별 설정 값은 이 설정을 기반으로 함
- 관련 객체
    - AuthorizationEndpointConfig : authorization grant 관련 설정 (authorization code 등)
    - TokenEndpointConfig : access token 관련 설정
    - RedirectionEndpointConfig : auth -> client redirect 관련 설정
    - UserInfoEndpointConfig : user info 관련 설정

## OAuth2 Resource Server

Filter

OAuth2AuthenticationProcessingFilter
- protected resource 접근 요청 처리, access token 검증
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
