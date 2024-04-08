## 스프링 빈

스프링 컨테이너에 의해 관리되는 인스턴스

관심사 분리
* 객체 생명주기, 의존성 주입 : IoC 컨테이너
* 비즈니스 로직 : 개발자

### Bean Defiinition

bean에 대한 configuration metadata로 IoC 컨테이너에서 스프링 빈마다 BeanDefiniiton으로 정의됨

bean definition
* Class
* Name
    * @Component : 첫 글자를 소문자로 변경한 이름
    * @Bean : 메서드 명
* Scope
    * 기본 값 : singleton
* Constructor arguments, Properties
    * 필요한 의존성, 프로퍼티 명시
* Autowiring mode
* Lazy initialization mode
    * 기본 모드 : 스프링 컨테이너가 로드될 때 초기화, 등록
    * Lazy mode : 실제 스프링 빈이 사용될 때 초기화, 등록
* Initialization, Destruction Method
    * 생명주기 콜백

## 빈 등록 방법
- XML
- Java Config
- Annotation

### Java Config

1. @Configuration

스프링 빈들을 등록할 configuration 클래스 명시

@Configuration 클래스 또한 스프링 빈으로 등록됨

주요 속성
- proxyBeanMethods(default true)
    - @Configuration 클래스의 프록시를 만드는 속성
    - @Bean으로 등록한 객체를 싱글톤 스코프로 동작하기 위함
    - 덕분에 여러 번 @Bean 메서드를 호출하더라도 동일한 객체를 반환받음
    - 속성 값을 false로 지정하면 @Bean 메서드를 호출 할 때마다 새로운 객체를 생성한다

- enforceUniqueMethods(default true) 
    - 고유한 @Bean 메서드 이름을 가지도록 하는 속성
    - 의도치 않은 오버로드를 방지함

2. @Bean

스프링 빈으로 등록할 클래스 명시

@Configuration 클래스 내부에서 선언하는 것을 권장

메서드, 어노테이션에만 적용 가능

보통 외부 라이브러리 객체를 스프링 빈으로 등록할 때 사용

주요 속성
- name
    - 빈 이름 지정, 여러 개 가능

### Annotation

1. @Component

스프링 빈으로 등록할 클래스 명시

클래스에만 적용 가능

애플리케이션 내의 객체를 스프링 빈으로 등록할 때 사용

2. @Component Scan

자동으로 컴포넌트를 찾아 스프링 빈으로 등록하는 기능을 가진 어노테이션

Component Scan 대상
- @Component
- @Controller
- @Service
- @Repository
- @Configuration
- @RestController
- @ControllerAdvice
- @RestControllerAdvice

기본 범위
- @ComponentScan이 선언된 클래스의 패키지와 하위 패키지 스캔

주요 속성
- basePackages
    - Component Scan를 시작할 패키지 지정(여러 개 가능)
- basePackageClasses
    - 패키지 대신 클래스 지정
    - 해당 클래스가 속한 패키지를 Scan 시작점으로 지정
- includeFilters, excludeFilters
    - Component Scan 시 포함, 제외 컴포넌트 지정
    - 어노테이션, 타입, 정규표현식, AspectJ 패턴, 커스텀 활용
    - include, exclude 둘 다 포함되는 경우 exclude 속성이 우선순위를 가짐
- useDefaultFilters (기본 값 true)
    - @Component, @Controller, @Service, @Repository 등이 붙은 클래스 자동 Scan
    - false 지정 시 어노테이션이 붙은 대상 무시, 필터로만 등록 가능

## 빈 스코프

singleton (기본 값)
- 1:1 - bean definition : 빈 인스턴스
- 동일한 인스턴스 의존성 주입
- singleton 빈이 prototype 빈을 의존하는 경우
    - IoC 컨테이너는 singleton 빈을 생성할 때만 의존성 주입을 해줌
    - 매번 prototype 빈을 주입받아야 되는 경우 method injection 필요

prototype
- 1:N - bean definition : 빈 인스턴스
- 매번 새로운 인스턴스 의존성 주입


request, session, application, websocket scope는 WebApplicationContext에서만 유효함

request
- 1:N - http request : 빈 인스턴스
- @RequestScope

session
- 1:N - http session : 빈 인스턴스
- @SessionScope

application
- 1:N - servlet context : 빈 인스턴스
- @ApplicationScope

websocket
- 1:N - WebSocket : 빈 인스턴스
- [websocket scope](https://docs.spring.io/spring-framework/reference/web/websocket/stomp/scope.html)

## 빈 생명주기 콜백

IoC 컨테이너가 빈 생성, 소멸할 때 호출하는 콜백

인스턴스 생성, 의존성 주입 후 초기화 콜백 호출

1. 인터페이스 구현
2. 메서드 지정
    - 주로 @Bean으로 스프링 빈 등록 시 사용
    - initMethod, destroyMethod 속성
3. @PostConstruct, @PreDestroy 
    - 클래스 내 콜백을 수행하는 메서드에 선언

## Dependency Injection

### Injection Annotation

@Autowired
- Spring 어노테이션
- 생성자, 메서드, 파라미터, 필드, 어노테이션 타입 적용 가능
- 타입, 참조변수 이름, @Qualifier, @Primary 순 탐색
- required 속성 존재(의존성 주입 필수 여부 지정)

@Inject
- Java 어노테이션
- 생성자, 메서드, 필드 적용 가능
- 타입, 참조변수 이름, @Qualifier, @Primary, @Named 순 탐색

@Resource
- Java 어노테이션
- 타입, 메서드, 필드 적용 가능

### Constructor Injection

객체 생성 시점에 생성자를 통해 의존성 주입을 받는 방법(권장)

- 필요한 의존성들을 생성자에 깔끔하게 명시
- 객체 생성 시 모든 의존성을 필수로 받음
    - 순환참조, 의존성 주입 실패 시 오류 발생 -> 런타임 에러 방지
- 생성자가 하나라면 @Autowired 생략 가능(스프링 4.3 이후)

final 키워드 선언 권장
- constant한 의존성 참조변수
- 의존성 주입은 final과 무관하게 동작

```
private final LocalService;

public LocalController(LocalService localService) {
    this.localService = locaService;
}
```

### Field, Property Injection

객체를 생성한 뒤 필드에 의존성 주입을 받는 방법

권장하지 않는 이유
- final 키워드 사용 불가 
- 의존성 누락 가능성
- 순환 참조
- 애플리케이션 로드 시점이 아닌 런타임 시점에 에러가 발생함

```
@Autowired 
private LocalService localService;
```

### Method injection

객체를 생성한 뒤 메서드를 통해 의존성 주입을 받는 방법

용도
- 선택적 의존성 주입
- 여러 의존성 주입

권장하지 않는 이유
- 코드 복잡도 증가
- 불확실한 의존성 주입

```
private LocalService localService;

@Autowired
public void SetLocalService(LocalService localService) {
    this.localService = localService;
}
```

## Injection Priority

IoC 컨테이너는 의존성 주입 시 타입을 기반으로 함

동일한 타입의 빈이 여러 개 있을 경우 -> NoUniqueDefinitionException 발생
- 인터페이스, 부모 클래스 타입으로 주입받는 경우 
- 컴포넌트 스캔과 Java Config 설정 간의 타입 중복

해결 방안

@Primary
- 동일한 타입의 빈 중에서 우선순위를 가짐

```
@Primary
@Component
public class FirstLocalService implements LocalService

@Component
public class SecondLocalServie implements LocalService
```

@Qualifier
- 빈 이름으로 주입받을 빈 선택

```
public LocalController(@Qualifier("secondLocalService") localService) {
    this.LocalService = localService;
}
```

@Named
- @Qualifier와 동일, @Inject에서 사용 가능

```
@Inject
public LocalController(@Named("secondLocalService") localService) {
    this.LocalService = localService;
}
```
