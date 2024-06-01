## Spring Boot Starter, Module

스프링 부트 테스트 스타터
- `spring-boot-starter-test`
  - 포함된 의존성 : Spring Test, Spring Boot, JUnit5, AssertJ, Hamcrest, Mockito, JSONassert, JsonPath, Awaitility, xmlunit    
  - 의존성 선언 : `testImplementation 'org.springframework.boot:spring-boot-starter-test'`

테스트 모듈
- `spring-boot-test` : 스프링 부트 테스트 코어 모듈
- `spring-boot-test-autoconfigure` : 스프링 부트 테스트 자동 설정 모듈

## Spring Context Caching

Spring TestContext Framework는 Test Suite(테스트 클래스)에 대한 ApplicationContext(or WebApplicationContext)를 로드한 뒤 static cache에 저장함

저장된 컨텍스트는 동일한 Test Suite내에서 동일한 Unique Context Configuration을 사용하는 모든 하위 테스트에서 재사용됨

또한 서로 다른 Test Suite들도 ApplicationContext에 대한 Configuration이 동일하다면 재사용됨

**ApplicationContext Unique Identifier**

ApplicationContext를 재사용할 수 있는 이유

ApplicationContext를 static cache에 저장할 때 Test Suite의 Configuration Parameter를 조합하여 생성된 고유 키를 사용함

테스트 클래스마다 Configuration이 다르지 않은 경우 동일한 키를 가지므로 캐시된 컨텍스트를 재사용함

Configuration Parameters
- location (from @ContextConfiguration)
- classes (from @ContextConfiguration)
- contextInitializerClasses (from @ContextConfiguration)
- activeProfiles (from @ActiveProfiles)
- propertySourceProperties (from @TestPropertySource)
- resourceBasePath (from @WebAppConfiguration)
- ...

## @SpringBootTest

springboot의 기능을 더한 spring-test의 @ContextConfiguration 대체 어노테이션

**ApplicationContext**
- 스프링 부트 애플리케이션은 Spring ApplicationContext이므로 일반 Spring Context에서 수행하는 것 외에 특별히 테스트할 필요가 없음
- SpringApplication을 사용해서 테스트에 사용할 ApplicationContext를 생성함

**WebEnvironment 속성**
- 테스트 시 임베디드 서버의 로드 여부를 결정하는 속성
- MOCK(default)
  - Web ApplicationContext 로드, Mock Web Environment 제공(실제 임베디드 서버 실행 X)
  - mock 기반 web application 테스트를 위한 @AutoConfigureMockMvc, @AutoConfigureWebTestClient 선언 가능
  - 테스트 클래스에 선언하면 통합 테스트에서 사용되는 MockMvc, WebTestClient(WebFlux 용)를 필드, 파라미터 등으로 주입받을 수 있음
  - classpath에서 웹 환경을 사용하지 않는 경우 non-web ApplicationContext으로 전이됨
- RANDOM_PORT
  - WebServerApplicationContext 로드, random port로 실제 임베디드 서버 실행
  - `@LocalServerPort`을 통해 실제 서버의 포트를 필드에 주입받아서 확인할 수 있음
- DEFINED_PORT
  - WebServerApplicationContext 로드, application.properties에 정의된 port(또는 기본 8080)로 실제 임베디드 서버 실행
- NONE
  - ApplicationContext 로드, 어떤 웹 환경도 제공하지 않음

**UseMainMethod 속성**

일반적으로 @SpringBootTest가 찾은 Test Configuraiton은 main @SpringBootApplication이 됨

@SpringBootApplication이 선언된 클래스는 main 메서드를 통해 SpringApplication.run()을 호출함

다만 @SpringBootTest는 main 메서드를 호출하지 않고 해당 클래스 자체를 사용하여 ApplicationContext를 생성함

main 메서드를 실행해야 될 경우 속성 값을 변경
- UseMainMethod.NEVER(default)
- UseMainMethod.ALWAYS
- UseMainMethod.WHEN_AVAILABLE

## TestConfiguration

스프링 부트의 `@*Test` 어노테이션은 테스트가 포함된 패키지부터 @SpringApplication 또는 @SpringBootConfiguration 어노테이션이 있는 패키지까지 찾음

따라서 스프링의 `@ContextConfiguraiton(classes=...)` 처럼 @Configuration을 로드하기 위한 설정이 필요하지 않음

별도의 Configuration이 필요한 경우 해당 테스트 클래스에 @TestConfiguration을 선언한 내부 클래스를 생성하여 사용

참고로 @TestConfiguration은 @Component가 포함된 복합 어노테이션이므로 컴포넌트 스캔의 대상이 됨

만약 컴포넌트 스캔 대상에서 제외하고 싶다면 아래처럼 @Import에 명시하면 됨

```java
@SpringBootTest
@Import(MyTestsConfiguration.class)
class MyTests {

	@Test
	void exampleTest() {
		// ...
	}

}
```

## Mocking, Spying Spring Beans

특정 스프링 빈을 mocking, spying할 수 있음

@MockBean : ApplicationContext Bean에 대한 Mockito Mock 정의
- 필드로 주입받는 경우 : 각 테스트 메서드 실행 이후 자동적으로 reset됨

@SpyBean : ApplicationContext Bean에 대한 Mockito Spy 정의
- spring의 proxy 처리가 된 Bean에 @SpyBean을 선언했을 때 프록시를 제거하고 싶은 경우
- given 또는 when절에 `AopTestUtils.getTargetObject(yourProxiedSpy)` 사용

**참고사항**

Spring은 테스트 간에 application context를 캐시하고, 동일한 configuration을 공유한 경우 재사용함

@MockBean과 @SpyBean은 cache key에 영향을 주기 때문에 context의 수를 늘릴 가능성이 높음

## Test With @Transactional

**@SpringBootTest의 WebEnvironment 속성에 따른 동작 방식 차이** 
- WebEnvironment.MOCK인 경우
  - 각 테스트 메서드 실행 후 트랜잭션 롤백
- WebEnvironment.RANDOM_PORT, WebEnvironment.DEFINED_PORT인 경우
  - 실제 임베디드 서버를 사용하므로 서로 다른 스레드에서 HTTP client와 Server가 실행됨
  - 따라서 이런 경우 트랜잭션이 분리되므로 서버에서 시작된 트랜잭션을 롤백을 할 수 없음

## Auto-configured Tests

특정 부분에 대한 테스트(slice test)만 가능하도록 자동 설정해주는 기능

`spring-boot-test-autoconfigure` 모듈에서 테스트 목적에 따른 어노테이션이 제공됨

각 어노테이션은 `@*Test`로 끝나며 ApplicationContext를 로드하고, 자동 설정을 위한 `@AutoConfigure...` 어노테이션이 하나 이상 포함됨

**참고사항**
- 각 slice에 맞는 컴포넌트와 auto-configuration class만 제한해서 스캔/로딩함
  - 해당 slice 어노테이션에서 제공하는 auto-configuration을 제외하려면 excludeAutoConfiguraiton 속성 또는 @ImportAutoConfiguration의 exclude 속성에 지정
- 여러 개의 slice 어노테이션 선언 불가능 -> 하나의 어노테이션만 선언하고, @AutoConfigure을 추가해야 됨
- slicing을 하지 않지만 auto-configured test bean이 필요한 경우
  - `@AutoConfigure...`을 별도로 선언
  - `@ImportAutoConfiguration({CertainAutoConfiguration.class, ...})` 선언
  - 클래스 이름은 `spring-boot-test-autoconfigure/META-INF/spring` 하위에 기능별로 나뉜 imports 파일에서 확인할 수 있음

참고

[spring boot test docs](https://docs.spring.io/spring-boot/reference/testing/index.html)

[spring test context caching](https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/ctx-management/caching.html)