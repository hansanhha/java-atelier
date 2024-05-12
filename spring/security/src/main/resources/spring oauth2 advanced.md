## OAuth2 Login Success Handling

### 로그인 성공 후처리

AuthenticationManager(ProviderManager) - 이벤트 발행
- DefaultAuthenticationEventPublisher 사용

AbstractAuthenticationProcessingFilter(OAuth2LoginAuthenticationFilter 부모) - 이벤트 발행 및 successHandler 호출
- ApplicationContext(AnnotationConfigServletWebServerApplicationContext) EventPublisher 사용
- successHandler : SavedRequestAwareAuthenticationSuccessHandler 구현체 사용 

### SuccessHandler

#### SavedRequestAwareAuthenticationSuccessHandler (Spring Security)

원래 request의 목적지로 리다이렉트할 때 사용하는 클래스

인증 프로세스 전 ExceptionTranslatorFilter에 의해 세션에 저장된 DefaultSavedRequest 객체 사용

request를 가로챈 뒤 request 데이터, 목적지 저장 -> 동일한 URL로 리다이렉트할 때 사용

인증 성공 시 리다이렉트 결정 시나리오
- RequestCache에 SavedRequest가 있을 시 : targetParameterUrl 추출
  - targetUrlParameter 값이 없으면서 alwaysUseDefaultTargetUrl이 false일 시 : SavedRequest에 저장된 redirectUrl로 리다이렉트
  - 아닐 시 : RequestCache에 저장된 request 삭제, 상위 핸들러 호출
- RequestCache에 SavedRequest가 없을 시 : 상위 핸들러 호출

**동작 흐름**
1. 인증 시도 : AbstractAuthenticationProcessingFilter.attemptAuthentication()
    - OAuth2LoginAuthenticationFilter.attemptAuthentication()
2. OAuth2 인증 성공
3. SuccessHandler 호출 : AbstractAuthenticationProcessingFilter.successfulAuthentication()
    - SavedRequestAwareAuthenticationSuccessHandler.onAuthenticationSuccess()
4. 상위 SuccessHandler 호출 : SimpleUrlAuthenticationSuccessHandler.onAuthenticationSuccess()
5. 상위 Handler 호출 : AbstractAuthenticationTargetUrlRequestHandler.handle() 호출
6. 리다이렉트 실행 : DefaultRedirectStrategy.sendRedirect() 호출

#### SavedRequestAwareAuthenticationSuccessHandler 관련 클래스 (Spring Security)

**SimpleUrlAuthenticationSuccessHandler**

이름 그대로 간단한 SuccessHandler

동작 흐름
1. AbstractAuthenticationTargetUrlRequestHandler.handle() 호출
2. 세션에 저장된 attribute 제거(SPRING_SECURITY_LAST_EXCEPTION 제거)

**AbstractAuthenticationTargetUrlRequestHandler**

리다이렉트 url을 결정하고 리다이렉트하는 핸들러, SuccessHandler의 역할은 아님

커스텀 SuccessHandler를 만들 때 리다이렉트 기능을 이용하고자 상속

동작 흐름
1. 리다이렉트 url 결정 : determineTargetUrl()
   1. default url을 항상 사용한다면 defaultTargetUrl로 결정
   2. targetParameterUrl이 있다면 targetParameterUrl로 결정
   3. Referer 헤더를 사용한다면 Referer로 결정
   4. 그것도 없다면 defaultTargetUrl로 결정
2. 응답 상태 확인     : response.isCommitted() 
3. 리다이렉트         : redirectStrategy.sendRedirect()

기본 값
- isAlwaysUseDefaultTargetUrl : false
- targetUrlParameter (request 파라미터 이름): null
- defaultTargetUrl : "/"
- useReferer : false

**RedirectStrategy**

구현체 : DefaultRedirectStrategy

동작 흐름
1. redirect url 계산(절대 url, 상대 url)
2. redirect url 인코딩
3. 302 status code 시 redirect
4. 아닐 시 response flush

절대 url : 프로토콜부터 시작하는 전체 경로 URL

상대 url : 현재 URL을 기준으로 하는 경로

예시
```
contextPath : /app
절대 URL : http://example.com/app/login
상대 URL : /login

isContextRelative - false인 경우
상대 URL 예시: /app/login 반환
절대 URL 예시: http://example.com/app/login 반환

isContextRelative - true인 경우
상대 URL 예시: /login
절대 URL 예시: login 반환(절대 URL에서 프로토콜과 도메인, 컨텍스트 경로(/app)를 제외한 나머지 경로 반환)
```

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

OAuth2 인증을 완료하면 SuccessHandler를 통해 애플리케이션에서 JWT 토큰을 발급

두 가지 구현 방법
1. jjwt 라이브러리를 사용해서 직접 JWT 토큰 발급
2. spring-security-oauth2-jose(nimbus-jose-jwt)를 통해 JWT 토큰 발급

[jose](https://github.com/hansanhha/server/blob/main/auth/jose.md)

### Refresh Token With OAuth2AuthorizedClientManager
