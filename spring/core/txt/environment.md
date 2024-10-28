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