## Spring Security OAuth 2.0 Client 

### Authorization Grant Support

Authorization Code

Refresh Token

Client Credentials

Resource Owner Password Credentials

JWT Bearer

#### Client Authentication Support

JWT Bearer

### Client Workflow

Spring Security OAuth 2.0 동작 흐름은 Spring Security Architecture를 기반으로 함

Filter -> AuthenticationManager -> AuthenticationProvider -> UserDetailsService

SecurityContextHolder(SecurityContext, Repository, Authentication)

1. 사용자 로그인 요청(client application login thorough OAuth2 provider)
    - 카카오 로그인, 네이버 로그인, 구글 로그인 등
2. (OAuth2 필터 동작) 클라이언트 서버 -> 인증 서버 리다이렉트 요청
    - OAuth2AuthorizationRequestRedirectFilter
    - ClientRegistration.getProviderDetails().getAuthorizationUri()
3. 사용자 계정 인증
4. 사용자 클라이언트 권한 동의
5. (OAuth2 필터 동작) 인증 서버 -> 클라이언트 서버 리다이렉트 authorization code, state 전달
   - OAuth2LoginAuthenticationFilter
   - ClientRegistration.getRedirectUri()
     1. 클라이언트 서버 -> 인증 서버 authorization code, redirect uri 등으로 access token 요청
        - OAuth2AuthorizationCodeAuthenticationProvider
        - DefaultAuthorizationCodeTokenResponseClient
     2. 인증 서버 : 요청 데이터 검증 후 access token, refresh token 발급
     3. 클라이언트 서버 -> 인증 서버 사용자 정보 요청 with access token
        - OAuth2UserService, OidcUserService
        1. 인증 서버 : access token 검증, 사용자 정보 반환
        2. 클라이언트 서버 : 사용자 회원 여부 확인, 필요 시 회원가입 처리
     4. 클라이언트 서버 : OAuth2AuthorizedClient 생성, OAuth2AuthorizedClientRepository에 저장
        - ClientRegistration, access token, refresh token, OAuth2User principal

### Client

#### OAuth2AuthorizedClient

인증된 Client에 대한 정보

사용자 인증(클라이언트의 사용자 정보 접근 권한 부여), Provider로부터 access token을 받으면 인증된 Client임

ClientRegistration, OAuth2AcccessToken, OAuth2RefreshToken, Principal 포함

#### Oauth2AuthorizedClientManager

OAuth2AuthorizedClient를 전반적으로 관리(생성, 로딩, 저장)하는 역할

Spring Security AuthenticationManager -> AuthenticationProvider와 동일한 흐름

```java
@FunctionalInterface
public interface OAuth2AuthorizedClientManager {
    @Nullable
    OAuth2AuthorizedClient authorize(OAuth2AuthorizeRequest authorizeRequest);
}
```

동작 흐름
1. OAuth2AuthorizeRequest 생성
2. OAuth2AuthorizedClientProvider로 OAuth2AuthorizedClient 생성
3. OAuth2AuthorizedClientRepository 또는 OAuth2AuthorizedService를 통해 OAuth2AuthorizedClient 저장
4. OAuth2AuthorizationSuccessHandler 또는 OAuth2AuthorizationFailureHandler 호출

#### OAuth2AuthorizedClientProvider

OAuth2AuthorizedClient를 각 특정 방법으로 로딩, 생성하는 역할 

AuthorizationCode, RefreshToken, ClientCredentials, JWT Bearer 방식으로 생성 가능

```java
@FunctionalInterface
public interface OAuth2AuthorizedClientProvider {
    @Nullable
    OAuth2AuthorizedClient authorize(OAuth2AuthorizationContext context);
}
```

#### OAuth2AuthorizedClientService

애플리케이션 수준에서 OAuth2AuthorizedClient 인스턴스 저장, 검색하는 역할

구현체 : (default) InMemoryOAuth2AuthorizedClientService, JdbcOAuth2AuthorizedClientService

```java
public interface OAuth2AuthorizedClientService {
    <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName);

    void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal);

    void removeAuthorizedClient(String clientRegistrationId, String principalName);
}
```

#### OAuth2AuthorizedClientRepository

웹 요청 간 OAuth2AuthorizedClient를 저장, 검색하는 역할

```java
public interface OAuth2AuthorizedClientRepository {
    <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, Authentication principal, HttpServletRequest request);

    void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal, HttpServletRequest request, HttpServletResponse response);

    void removeAuthorizedClient(String clientRegistrationId, Authentication principal, HttpServletRequest request, HttpServletResponse response);
}
```

### Client Filter

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
  - OAuth2AuthorizedManager
  - OAuth2AuthorizedClientService
  - OAuth2AuthorizedClientProvider
  - OAuth2AuthorizedClientRepository 
  - OAuth2AuthorizedClient
  - OAuth2AuthorizationSuccessHandler
  - OAuth2AuthorizationFailureHandler

DefaultLoginPageGeneratingFilter
- 애플리케이션 로그인 페이지를 자동으로 생성하는 필터
- OAuth2 로그인(oauth2Login())이 구성된 경우 OAuth2 Provider 링크가 포함된 로그인 페이지 생성 가능
- 사용하지 않으려면 대신하는 로그인 페이지 지정
  - `/login/oauth2/code/*` 경로로 리다이렉션 필요

### Client Configuration

```java
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

리다이렉트 기본 로그인 페이지, 기본 경로
- 기본 페이지 : DefaultLoginPageGeneratingFilter가 base uri를 기반으로 provider(clientName)별 링크 생성
    - 예시 : `<a href="/oauth2/authorization/google">Google</a>`
    - 커스텀 : OAuth2LoginConfigurer.loginPage()
- OAuth2 provider로 리다이렉트할 기본 url : `/oauth2/authorization/{registrationId}
  - 커스텀 : OAuth2LoginConfigurer.authorizationEndpoint(AuthorizationEndpointConfig.baseUri()) 
- 인증 후 리다이렉트될 기본 url : `login/oauth2/code/{provider}`
  - 커스텀 : OAuth2LoginConfigurer.loginProcessingUrl()

```java
import org.springframework.security.config.Customizer;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
            .authorizeHttpRequests(Customizer.withDefaults())
            .oauth2Login(oauth2Login -> oauth2Login
                    .loginPage("/login/oauth2")
                    .authorizationEndpoint(authorization -> authorization
                            .baseUri("/login/oauth2/authorization")
                    )
            );

    return http.build();
}
```

## Spring Security OAuth 2.0 Resource Server



### Resource Server Filter

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
