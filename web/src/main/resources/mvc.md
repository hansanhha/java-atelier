## Static Web Page

웹 서버에서 정적 웹 페이지의 렌더링을 하고 반환하는 기술

## CGI(Common Gateway Interface)

웹 서버의 동적 컨텐츠 처리를 위한 기술

동작 구조
1. 요청
2. 웹 서버 수신 및 대응되는 CGI 프로그램 확인
3. 운영체제에 의해 CGI 프로그램 실행(동적 처리)
4. 렌더링된 HTML 반환

단점
- 요청마다 CGI 프로세스가 실행됨
- 스크립트 언어 CGI 프로그램의 경우 매번 스크립트를 해석해야 됨
- C, Perl, Shell Script로 개발했는데, 이 언어들은 대규모 웹 서버 설계에 적합하지 않음

웹 서버에서 임의의 프로그램을 실행할 수 있기 때문에 간혹 쓰이는 경우도 있음 

## Server Side Scripting

웹 서버 자체에서 동적 페이지를 처리하는 기술

요청마다 프로세스를 생성하여 운영체제를 통해 실행하지 않고 웹 서버 내의 스레드를 통해 직접 처리

자바같은 객체지향 언어으로 대규모 웹 서버를 개발

Servlet
- Java EE 사양의 일부로 HTTP를 처리하고 응답을 생성하는 서버 컴포넌트
- HTML 페이지 생성, 세션 관리, DB 처리 등
- 자바로 작성되며 JVM 위에서 실행
- DispatchType : 서블릿 요청이 처리되는 방식
    - REQUEST : 기본 디스패치 타입
    - FORWARD : 다른 Servlet이나 JSP로 요청을 전달할 때 사용(RequestDispatcher.forward())
    - INCLUDE : 다른 Servlet이나 JSP를 응답에 포함시킬 때 사용(RequestDispather.include())
    - ERROR   : 에러가 발생했을 때(/error)
    - ASYNC   : 비동기 처리(Servlet 3.0 이상)

JSP
- JSP는 HTML 페이지에 자바 코드를 삽입하는 방식, Servlet은 자바 클래스에 HTML을 삽입하는 방식
- 자바에서 HTML 관련 코드를 Servlet보다 편리하게 작성할 수 있는 Server Side Scripting 기술
- Servlet을 기반으로 하며 JSP 페이지는 Servlet으로 변환되어 실행됨
- MVC 아키텍처를 지원함
- JSP - View, Servlet - Controller

## MVC 

두 가지의 관심사(비즈니스 로직, 화면)를 분리하는 디자인패턴

구성 요소
- Model : 데이터와 비즈니스 로직 처리
- View  : 화면 처리
- Controller : 요청 수신/검증 및 Model과 View에게 명령 전달

동작 구조
1. 컨트롤러 요청 수신
2. 컨트롤러 -> 모델 데이터 변경
3. 컨트롤러 -> 뷰 화면 처리

SSR(서버 사이드 렌더링) : 서버에서 화면 처리 담당

CSR(클라이언트 사이드 렌더링) : 클라이언트에서 화면 처리 담당

## Spring MVC, FrontController

MVC 패턴의 Controller는 path(요청 url)에 마다 개별 Controller를 만듦

-> Controller가 수행하는 검증, 세션 관리, 필터 등의 코드가 중복됨

[FrontController](https://martinfowler.com/eaaCatalog/frontController.html)는 모든 요청을 처리하는 Controller를 두는 디자인패턴으로 코드 중복없이 재사용할 수 있는 유연한 구조를 가짐

스프링 MVC 구성요소
- DispatcherServlet
    - FrontController 역할
    - 요청에 맞는 Component 검색 및 실행
    - View 해결
    - 에러 처리 등
- Handler Mapping
    - 요청(URL)을 어느 Controller의 메서드가 처리할 지 결정
    - RequestMappingHandlerMapping : @RequestMapping 기반 어노테이션이 적용된 컨트롤러 메서드 매핑
- Controller(Handler)
    - 요청을 처리하는 컴포넌트
    - URL 별로 로직을 수행할 메서드
- View Resolver
    - Controller가 반환하는 View 이름을 기반으로 실제 View를 찾는 역할
    - View 파일을 렌더링하여 클라이언트에게 응답
    - InteralResourceViewResolver : View 이름을 기반으로 내부 리소스를 찾는 View Resolver
- View
    - View 파일

스프링 MVC 계층 구조
- Presentation Layer
    - 요청을 처리하는 계층
    - Controller, View
- Business Logic Layer
    - 애플리케이션 핵심 로직을 수행하는 계층
    - Service : 비즈니스 로직 집합
    - Domain Model : 비즈니스 로직/연산, 데이터
- Data Access Layer
    - DB 접근, 영속성 기술 수행 계층
    - DAO(Data Access Object) : 
    - Repository : 

DispatcherServlet과 WebApplicationContext 관계
1. 각 DispatcherServlet은 자신만의 Servlet WebApplicationContext가 존재
2. 또한 애플리케이션 로딩 시점에 생성되는 Root WebApplicationContext를 부모로 설정

Root WebApplicationContext 
- 애플리케이션의 공통 컴포넌트(서비스 계층, 데이터 액세스 계층)
- ContextLoaderListnener에 의해 초기화
    - 스프링 - web.xml, application-context.xml
    - 스프링부트 - @SpringBootApplication

Servlet WebApplicationContext
- 프레젠테이션 계층(Controller, View Resolver, Handler Mapping)
- DispatcherServlet 초기화 시 자동 생성
    - 스프링 - dispatcher-servlet.xml
    - 스프링부트 - @EnableAutoConfiguration을 통해 내부적으로 DispatcherServlet 초기화 및  Servlet WebApplicationContext 자동 처리
- Servlet WebApplicationContext 빈은 특정 Servlet에서만 접근할 수 있음

## Filter

Servlet과 마찬가지로 Java/Jakarata EE 스펙에 포함됨

특징
- HTTP Request 사전 처리, 사후 처리를 담당하는 요소(Servlet 실행 전/후 동작)
- url(path)에 매핑되는 Filter 동작
- 다음 Filter의 실행을 중단하거나 실행 결과를 무시하고 직접 응답을 반환할 수 있음
- ServletContainer 내에서 동작

FilterChain
- FilterChain은 여러 Filter가 순서대로 연결된 체인 형태로 작동
- Servlet은 모든 FilterChain의 Filter가 실행된 이후 마지막에 호출됨

## Interceptor

Spring MVC 스펙에 포함됨

특징
- DispatcherServlet에서 Controller(Handler)로 요청 위임 전/후/완료 후에 동작하는 요소
- url(path)에 매핑되는 Interceptor 동작
- 스프링 웹 애플리케이션 컨텍스트 내에서 동작함

시점
- 요청 전 : Controller가 요청을 처리하기 전
- 요청 후 : Controller가 요청을 정상적으로 처리한 후(View 렌더링 전) 
- 요청 완료 후 : 예외 발생 여부와 무관하게 Controller가 요청을 처리한 후

차이점
- Filter : 스프링 컨텍스트 도달 전/후로 필요한 공통 로직 처리 용도
- Interceptor : 스프링 컨텍스트 내에서 필요한 공통 로직 처리 용도

## Error, Exception Handling

웹 서버의 에러
- 서버 자체(네트워크 레벨 등) 문제
- 서버가 요청을 제대로 처리할 수 없는 상황
- web.xml
    - HTTP Status Code 별로 지정된 에러 페이지 반환

웹 애플리케이션에서 예외가 발생할 수 있는 부분
- Filter
    - FilterChain.doFilter() 생략 -> Servlet, Controller 등 호출 X
    - 직접 HttpServletResponse을 통해 Status Code, Header, Message Body 반환
    - 웹 서버는 Filter에서 처리한 응답을 그대로 클라이언트에게 반환
- Interceptor
    - 시점에 따른 에러 처리
    - 요청 전 에러 처리 : Controller 호출 X, HttpServletResponse로 직접 응답 반환
    - 요청 완료 후 에러 처리 : 클라이언트에게 응답 반환 후 호출되므로 직접 응답 반환 불가, 리소스 정리, 로깅 등 후처리 작업 수행
- Controller, 비즈니스 로직
    - web.xml, Filter, Interceptor, BasicController, @ControllerAdvice 중 선택하여 에러 처리 가능
    - Controller와 그 이후에서 예외 발생 시 DispatcherServlet은 처리되지 않은 모든 예외를 Catch함
    - 적절한 @ControllerAdvice, @ExceptionHandler를 검색하고 예외 처리 수행 -> 응답
    - 등록된 Interceptor의 afterCompletion() 호출

BasicController
- 스프링부트 애플리케이션 내부에서 예외 발생 시 내장된 웹 서버(Tomcat)까지 예외 전달, /error 경로로 요청을 재전송(DispatcherType.ERROR)
- 웹 서버에서 재전송한 요청은 ErrorController 인터페이스를 통해 에러 처리 가능
- BasicController는 스프링부트에서 제공하는 ErrorController 구현체
    - 클라이언트에게 응답할 Content-Type이 text/html이라면 View를 찾음
    - 아니라면 ResponseEntity 반환

@ControllerAdvice, @ExceptionHandler
- 



## Data Binding

### DTO, VO, Command Object

## Type Conversion, Formatting

## Spring MVC Workflows

## RESTful Spring MVC

### Error Handling

### Workflows
- 
