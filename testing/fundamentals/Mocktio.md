## Terminology

**SUT(System Under Test)**
- 테스트를 하는 대상 - Class, Method, Application

**DOC(Dependent On Component)**
- SUT가 의존하는 객체

**Interaction Point**
- 테스트가 SUT와 상호작용하는 지점, Control Point와 Observation Point로 나뉨
- Control Point : SUT에게 작업을 요청하는 지점 - Set Up, Exercise SUT, Tear Down 단계가 될 수 있음
- Observation Point : Exercise SUT 후 상태를 검사하는 지점 - Result Verification 단계가 될 수 있음

**Indirect Input**
- SUT의 동작이 다른 컴포넌트가 반환하는 값으로부터 영향을 받는 경우 해당 값을 Indirect Input이라고 함
- 함수 반환 값, DOC에서 발생한 예외 등

**Indirect Output**
- SUT의 동작을 SUT의 공개 API를 통해 관찰할 수 없지만 다른 시스템이나 애플리케이션 구성 요소에서 보거나 관련 수행이 포함된 경우 그 동작을 Indirect Output이라고 함
- 메시지 채널에 전송한 메시지, DB에 insert된 레코드 등

**테스트 구조**
- Test Instance 생성 : TestRunner -> Testcase Class(test method1...test method2) -> Test Suite Object (Test Case Object...(test method1...))
- Test 실행 : Test Runner -> Test Suite Object

Testcase class : SUT를 테스트하는 메서드를 포함하는 클래스, 런타임 시 test method 당 Testcase Object를 생성한 뒤 Test Suite Object에 추가(Test Runner가 테스트를 실행할 때 사용)

**Test Method의 네 가지 단계(Four-Phase Test)**
- Fixture Setup : 테스트 실행 전 필요한 상태 설정
- Exercise SUT : 테스트 대상 실행
- Result Verification : 테스트 결과 검증
- Fixture Teardown : 테스트 실행 후 상태 정리

예시
```java
public class UserServiceTest {

    private final UserService userService;
    private final UserRepository userRepository;

    @Before
    public void setUp() {
        // Fixture Setup
        userRepository = new UserRepository();
        userService = new UserService(userRepository);
    }

    @Test
    public void testMethod() {
        // Exercise SUT
        var Response = userService.getUser(1);
        // Result Verification
        assertThat(Response).isNotNull();
    }

    @After
    public void tearDown() {
        // Fixture Teardown
        userRepository.clear();
    }
}
```

**Test Double**
- DOC를 대체하는 객체
- 테스트해야 할 대상이 의존하는 객체로 인해 테스트가 어려울 때, 그 객체를 대체하기 위해 사용

**Test Stub**
- SUT의 DOC에 대한 indirect input을 지정하기 위한 control point 용도로 사용
- DOC의 반환 값이나, 예외를 지정

#### Test Spy
- Stub에 SUT의 실행 후 indirect output을 캡처할 수 있는 버전(나중에 verfication 가능)
- DOC의 일부 메서드를 Stub으로 대체 가능, Stub하지 않은 메서드는 실제 DOC의 메서드가 수행됨

**Mock Object**
- SUT가 실행될 때 indirect output을 확인하기 위해 observation point 용도로 사용
- 일반적으로 Mock은 Stub의 기능을 포함하고 있음 - 따라서 SUT에게 값을 반환해야 됨
- 다만 indirect output을 verification을 하는 것에 초점을 맞춤(Stub에 단순히 assertion을 추가한 게 아니라 근본적으로 다른 방식으로 사용됨)

**Fake Object**
- SUT의 indirect input/output을 verification하기 위해 실제 DOC의 기능을 가볍게 구현한 객체임
- 보통 실제 DOC를 사용하지 못하는 상황일 때 간단한 테스트용으로 사용(인메모리 데이터베이스)
- 테스트에서 control point나 observation point로 사용되지 않음

**Dummy Object**
- SUT의 메서드 시그니처를 충족하기 위해 사용되는 객체(테스트 더블보단 Value Object에 가까움)
- 실제로 사용되지 않음

## Mockito

자바 unit test mocking 프레임워크

테스트에 필요한 mock을 직관적으로 사용할 수 있음

주요 개념
- 원하는 것들만 verify 가능([verify what you want](https://szczepiq.wordpress.com/2008/02/24/can-i-test-what-i-want-please/))
- stubbing, verification만 있음(no expectation)
- mockito는 [Test Spy](#test-spy)를 구현함([자세한 내용](http://xunitpatterns.com/Mocks,%20Fakes,%20Stubs%20and%20Dummies.html))

## Mockito Feature

`@Mock` : 해당 객체 mocking

`@Spy`  : 해당 객체 spying

`@Captor` : 해당 객체 캡처

`InjectMocks` : mock, spy 객체 주입(스프링의 @Autowired와 비슷)


Programmatic stubbing
```java
Mockito.when(mock.action()).thenReturn(true)
BDDMockito.given(mock.action()).willReturn(true)
```

Programmatic verification
```java
Mockito.verify(mock).action()
BDDMockito.then(mock).should().action()
```

## Mocikto 규칙

1. 직접 구현하지 않은 타입에 mocking하지 않기
2. value object에 mocking하지 않기(대신 dummy object 사용)
3. 모든 걸 mocking하지 않기

## JUnit5 + Mockito 사용 예시

의존성 추가(버전 명시 필요)
```groovy
testImplementation 'org.junit.jupiter:junit-jupiter:5.9.3'
testRuntimeOnly 'org.junit.platform:junit-platform-launcher'

testImplementation 'org.mockito:mockito-core:5.12.0'
testImplementation 'org.mockito:mockito-junit-jupiter:5.12.0'
```

[테스트 코드 1](../mockito/src/test/java/mockito/user/UserServiceTest.java)

[테스트 코드 2](../mockito/src/test/java/mockito/order/OrderServiceTest.java)

## 참고

[xUnit Patterns](http://xunitpatterns.com/index.html)
[Mockito github wiki](https://github.com/mockito/mockito/wiki)