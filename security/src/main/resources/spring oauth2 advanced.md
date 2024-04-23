## OAuth2 Login Success Handling

### 로그인 성공 후처리

AuthenticationManager(ProviderManager) - 이벤트 발행
- DefaultAuthenticationEventPublisher 사용


AbstractAuthenticationProcessingFilter(OAuth2LoginAuthenticationFilter 부모) - 이벤트 발행 및 successHandler 호출
- ApplicationContext(AnnotationConfigServletWebServerApplicationContext) EventPublisher 사용
- successHandler : SavedRequestAwareAuthenticationSuccessHandler 구현체 사용 

### SuccessHandler

#### AuthenticationSuccessHandler (Spring Security)

인증 성공 시 AbstractAuthenticationProcessingFilter.successfulAuthentication()에서 호출

동작 흐름(기본 구현체 SavedRequestAwareAuthenticationSuccessHandler인 경우)
1. 인증 시도 : AbstractAuthenticationProcessingFilter.attemptAuthentication()
   - OAuth2LoginAuthenticationFilter.attemptAuthentication()
2. OAuth2 인증 성공
3. SuccessHandler 호출 : AbstractAuthenticationProcessingFilter.successfulAuthentication()
   - SavedRequestAwareAuthenticationSuccessHandler.onAuthenticationSuccess()
4. 상위 SuccessHandler 호출 : SimpleUrlAuthenticationSuccessHandler.onAuthenticationSuccess()
5. 상위 Handler 호출 : AbstractAuthenticationTargetUrlRequestHandler.handle() 호출 
6. 리다이렉트 실행 : DefaultRedirectStrategy.sendRedirect() 호출

requestCache 및 session 관리, 리다이렉트 처리 등 수행

#### OAuth2AuthorizationSuccessHandler (Spring Security OAuth2)

OAuth2AuthorizedClientManager에서 호출

OAuth2 인증 프로세스에서 OAuth2AuthorizedClient 획득 후 실행

사용자 세션 관리, 토큰 저장, 특정 로직 수행 

이벤트, successHandler 처리

## Redirect Strategy

## Stateless Application

서버 세션에 데이터를 저장하는 대신 HTTP 헤더, 쿠키 등에 저장하는 방식

서버 간 상태를 공유할 필요가 없음 
1. 각 요청을 독립적으로 처리
2. 서버 확장 용이

Spring Security OAuth 2.0에서 stateful한 부분을 stateless하게 변경

### OAuth2AuthorizationRequestRepository 

authorization code 처리 중 OAuth2AuthorizationRequestRedirectFilter가 OAuth2AuthorizationRequest 저장

인증 서버로부터 리다이렉트된 후 OAuth2LoginAuthenticationFilter가 OAuth2AuthorizationRequest 삭제

기본 구현체는 HttpSessionOAuth2AuthorizationRequestRepository로 세션에 저장

### OAuth2AuthorizedClientRepository, OAuth2AuthorizedClientService

OAuth2LoginAuthenticationFilter가 access token 및 oauth2UserService 요청을 모두 마친 후 OAuth2AuthorizedClient 저장

이 때 사용되는 AuthenticatedPrincipalOAuth2AuthorizedClientRepository 구현체가 OAuth2AuthorizedClientService를 호출함  

이 때 사용되는 InMemoryOAuth2AuthorizedClientService 구현체는 Map 객체에 저장함

## JWT Bearer Token



### Refresh Token With OAuth2AuthorizedClientManager