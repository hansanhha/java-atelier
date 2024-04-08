## Environment

Profile
- 컨테이너에 등록할 bean definition 그룹 이름 
- 지정된 profile이 active되어야만 컨테이너의 빈으로 등록됨
- 테스트, 개발, 프로덕션 환경 구분 등으로 사용

Property
- 애플리케이션에서 사용되는 속성 값
- properties file
- JVM system properties
- system environment variables
- JNDI
- Map 객체 등

스프링 애플리케이션의 Environment는 위의 두 가지 관점을 추상화한 모델임
- Profiles
    - active할 profile 결정
    - 기본 profile 결정
- Properties
    - property source 구성
    - property 해결

## Profile

```
@Configuration
@Profile("development")
public class DataConfig {
    
    @Bean
    public DataSource dataSource() {
        ...
    }
}

@Component
@Profile("development")
public class ...
```

```
@Configuration
public class DataConfig {

    @Bean("dataSource")
    @Profile("development")
    public DataSource EmbeddedDataSource() {
        ...
    }

    @Bean("dataSource")
    @Profile("production")
    public DataSource MySqlDataSource() throws Exception{
        ...
    }
}
```
@Bean 메서드를 오버로드 하는 경우 빈 이름을 통일시켜 @Profile을 적용해야 올바른 동작을 함

### Profile Expression, Annotation

! : NOT, 해당 Profile이 아닐 때
& : AND
| : OR

&와 |를 같이 사용하려면 괄호 사용 필요 

```
@Profile("production & us-east")
@Profile("production & (us-east | eu-central)")
@Profile({"{p1, p2}"}) - p1 | p2와 동일
```

Profile 별 어노테이션
```
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile("production")
public @interface Production {
}
```

### Profile 활성화

1. spring.profiles.active 프로퍼티 
    - -Dspring.profiles.active="p1,p2"
2. ApplicationContext.getEnvironment.setActiveProfiles("p1", "p2")

### Default Profile

Default Profile : Default

Default Profile 이름 변경
1. spring.profiles.default
2. ApplicationContext.getEnvironment.setDefaultProfiles("development")
