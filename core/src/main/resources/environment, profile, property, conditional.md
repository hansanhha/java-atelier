## Environment

Profile
- 컨테이너에 등록할 bean definition 그룹 이름 
- 지정된 profile이 active되어야만 컨테이너의 빈으로 등록됨
- 테스트, 개발, 프로덕션 환경 구분 등으로 사용

Property
- 애플리케이션에서 사용되는 속성 값
- 다양한 PropertySource
- properties file
- JVM system properties
- system environment variables
- JNDI
- Map 객체 등

스프링 애플리케이션의 Environment는 위의 두 가지 요소를 추상화한 모델임
특정 Environment에 따른 Profile과 Property를 가짐

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

표현식을 통해 다양하게 Profile을 활성화할 수 있음

! : NOT, 해당 Profile이 아닐 때
& : AND
| : OR

&와 |를 같이 사용하려면 괄호 사용 필요 

```
@Profile("production & us-east")
@Profile("production & (us-east | eu-central)")
@Profile({"{p1, p2}"}) - p1 | p2와 동일
```

Profile 별 메타 어노테이션
```
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile("production")
public @interface Production {
}
```

### Profile 활성화

활성화 방법
1. spring.profiles.active 프로퍼티 지정 
    - -Dspring.profiles.active="p1,p2"
2. ApplicationContext를 통해 Environment API 사용
    - ApplicationContext.getEnvironment.setActiveProfiles("p1", "p2")

### Default Profile

Default Profile : Default

Default Profile 이름 변경 방법
1. spring.profiles.default 프로퍼티 지정
2. ApplicationContext를 통해 Environment API 사용
    - ApplicationContext.getEnvironment.setDefaultProfiles("development")

## Property

### PropertySource

key-value 쌍의 source를 추상화한 object로 스프링은 Enviroment별로 PropertySource로부터 properties를 로드함

주요 코드
```
public abstract class PropertySource<T> {
    protected final Log logger;
    protected final String name;
    protected final T source;

    public PropertySource(String name, T source) {
        this.logger = LogFactory.getLog(this.getClass());
        Assert.hasText(name, "Property source name must contain at least one character");
        Assert.notNull(source, "Property source must not be null");
        this.name = name;
        this.source = source;
    }

    public boolean containsProperty(String name) {
        return this.getProperty(name) != null;
    }

    @Nullable
    public abstract Object getProperty(String name);
```

Standardalone Application(StandardEnvironment)의 경우
- 두 개의 PropertySource를 가짐
- system properties(JVM system properties)
- system environment properties

Web Application(StandardServletEnvironment(StandardEnvironment 상속))의 경우
- Servlet Context Properties
- Servlet Config Properties
- JNDI

### Custom PropertySource

ConfigurableEnvironment는 PropertySource를 List로 보관하고 있는 MutablePropertySources를 가지고 있음

커스텀 PropertySource를 만든 뒤 추가 -> 가장 높은 우선순위를 가짐

```
ConfigurableApplicationContext ctx = new GenericApplicationContext();
MutablePropertySources sources = ctx.getEnvironment().getPropertySources();
sources.addFirst(new MyPropertySource());
```

### @PropertySource

@PorpertySoucre  : Environment에 PropertySource 파일을 추가하는 어노테이션
@PropertySources : 여러 개의 PropertySource 파일 추가

classpath: file: http: 등의 프로토콜을 지원함

@PropertySource로 파일을 추가한 뒤 Environment API를 사용하여 프로퍼티 사용
```
@Configuration
@PropertySource("classpath:/com/myco/app.properties")
public class AppConfig {

 @Autowired
 Environment env;

 @Bean
 public TestBean testBean() {
  TestBean testBean = new TestBean();
  testBean.setName(env.getProperty("testbean.name"));
  return testBean;
 }
}
```

PropertySource 파일 경로를 다른 Property로 사용하는 경우 Placeholder로 대체

아래의 경우 my.placeholder property를 PorpertySource로부터 가져와서 치환함

만약 없는 경우 app/path를 사용

propertySource에 정의되지 않았는데 기본 값이 없는 경우 IllegalArgumentException 발생
```
@PropertySource("classpath:/com/${my.placeholder:app/path}/app.properties")
```

### @Value

PropertySource의 Property를 필드에 직접 주입받는 방법

@PropertySource와 마찬가지로 classpath: file: http: 등의 프로토콜 지원

SpEL 사용 가능

```
@Component
public class MovieRecommender {

    private final String catalog;

    public MovieRecommender(@Value("${catalog.name}") String catalog) {
        this.catalog = catalog;
    }
}
```

**Property 값이 없는 경우** 

기본적으로 스프링이 Property 이름이 값을 주입함

값이 없는 경우 애플리케이션 초기화 에러를 일으키려면 별도 설정 필요

PropertySourcesPlaceholderConfigurer를 Java Config로 설정할 때 static으로 선언

```
@Configuration
public class AppConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
```

또는 기본 값 지정

```
@Value("${catalog.name:defaultCatalog}")

```

### Type Conversion Service 

property - 필드 타입 변환 
- primitive type은 spring에서 지원
- custom type의 경우 ConversionService 빈 등록 필요

```
@Configuration
public class AppConfig {

    @Bean
    public ConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(new MyCustomConverter());
        return conversionService;
    }
}
```

[스프링부트 @ConfigurationProperties]()

## Condition

@Conditional과 함께 사용되는 함수형 인터페이스로 특정 조건에 따라 해당 Bean을 등록하거나 무시하기 위해 사용됨

애플리케이션 로드 시점에 @Conditional이 적용된 클래스나 메서드에 대해서 Condition 구현체가 동작함

```
@FunctionalInterface
public interface Condition {
    boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);
}
```

ConditionContext : 현재 애플리케이션 컨텍스트와 관련된 정보(Environment, ResourceLoader, ClassLoader 등) 제공

AnnotatedTypeMetadata : 평가할 클래스나 메서드에 대한 메타데이터 제공 

ProfileCondition의 경우 해당 클래스나 메서드에 적용된 Profile을 가져온 뒤, 현재 Environment의 Profile에 포함되는 경우에만 true를 반환하고 빈으로 등록됨

```
class ProfileCondition implements Condition {
    ProfileCondition() {
    }

    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
        if (attrs != null) {
            Iterator var4 = ((List)attrs.get("value")).iterator();

            Object value;
            do {
                if (!var4.hasNext()) {
                    return false;
                }

                value = var4.next();
            } while(!context.getEnvironment().matchesProfiles((String[])value));

            return true;
        } else {
            return true;
        }
    }
}
```

### Conditional

Bean 등록과 관련된 @Bean, @Configuration, @Component에 선언하는 어노테이션

@Profile 어노테이션에 @Conditional과 ProfileCondition이 선언되어 있음(메타 어노테이션)

```
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({ProfileCondition.class})
public @interface Profile {
    String[] value();
}
```

[스프링부트 Conditional]()
