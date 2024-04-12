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

## FrontController, 스프링 MVC

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
2. 애플리케이션 로딩 시점에 생성되는 Root WebApplicationContext를 부모로 설정

Root WebApplicationContext 
- 애플리케이션의 공통 컴포넌트(서비스 계층, 데이터 액세스 계층)
- ContextLoaderListnener에 의해 초기화
    - 스프링 - web.xml, application-context.xml
    - 스프링부트 - @SpringBootApplication

Servlet WebApplicationContext
- 프레젠테이션 계층(Controller, View Resolver, Handler Mapping)
- DispatcherServlet 초기화 시 자동 생성
    - 스프링 - Dispatcher-servlet.xml
    - 스프링부트 - 내부적으로 DispatcherServlet 초기화 및  Servlet WebApplicationContext 자동 처리
- Servlet WebApplicationContext 빈은 특정 Servlet에서만 접근할 수 있음

## Error Handling

## Interceptor

## Filter

## RESTful Spring MVC
- 
