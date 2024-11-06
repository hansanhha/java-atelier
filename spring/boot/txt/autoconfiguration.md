[autoconfiguration](#autoconfiguration)

[autoconfiguration workflow](#autoconfiguration-workflow)

[참고](https://www.marcobehler.com/guides/spring-boot-autoconfiguration)

[참고](https://docs.spring.io/spring-boot/reference/features/developing-auto-configuration.html)

## autoconfiguration

자동 구성은 스프링 부트의 강력한 장점 중 하나로, 스프링 애플리케이션을 구동하기 위해 개발자가 해야 할 스프링이나 외부 라이브러리 등의 설정을 스프링 부트 차원에서 자동적으로 구성해주는 기능임

```java
@SpringBootApplication
public class BootApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}
```

IDE나 [spring initializr](https://start.spring.io)를 통해 스프링 부트 애플리케이션을 만들면 위와 같은 main 메서드가 자동으로 생성되는데, 

이를 실행하면 스프링 부트는 `@AucoConfiguration` 어노테이션이 붙은 빈들을 통해 자동 구성 기능을 실행함

또한 유연한 구성을 할 수 있도록 3가지 기능을 제공함

### 1. @PropertySource 자동 등록

순수 스프링 프레임워크에서 외부의 프로퍼티 파일에 접근하려면 @PropertySource 어노테이션을 통해 프로퍼티 파일의 위치를 스프링에게 알려주면 됨

스프링 부트 애플리케이션을 구동하면 스프링 부트는 자동적으로 17가지의 다양한 경로로 프로퍼티 소스를 등록함

개발자가 여러 경로를 통해 프로퍼티 값을 설정하면 그에 따라 자동 구성이 이뤄짐

### 2. @AutoConfiguration 읽기

모든 스프링 부트 기반 프로젝트는 `org.springframework.boot:spring-boot-autoconfigure` 의존성을 가지고 있음

이 의존성은 단순한 jar 파일로 스프링 부트 자동 구성에 대한 모든 것을 담고 있는데

`@AutoConfiguration` 빈들은 `org.springframework.boot:spring-boot-autoconfigure` jar 파일의 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 파일에 명시되어 있음

스프링 부트가 구동될 때 마다 이 파일을 읽고, 각 빈들의 조건들(@Conditional)을 평가하여 자동 구성을 이룸

### 3. 스프링 부트의 @Conditional

`@Conditional`은 스프링 프레임워크에서 제공하는 로우 레벨 어노테이션임

스프링 부트는 개발자가 다양한 조건문을 작성할 수 있도록 추가적인 `@Conditional` 어노테이션을 제공함

#### Class Conditions

`@ConditionalOnClass`: 클래스 패스에 지정한 클래스가 있는 경우 true 반환

`@ConditionalOnMissingClass`: 클래스 패스에 지정한 클래스가 없는 경우 true 반환

#### Bean Conditions

`@ConditionalOnBean`: ApplicationContext에 지정한 타입의 빈이 이미 있는 경우 true 반환

`@ConditionalOnMissingBean`ApplicationContext에 지정한 타입의 빈이 아직 없는 경우 true 반환

#### Property Conditions

`@ConditionalOnProperty`

#### Resource Conditions

`@ConditionalOnResource`

## autoconfiguration workflow