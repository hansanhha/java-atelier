스프링 데이터 Repository 설정
- [AbstractRepositoryConfigurationSourceSupport](#abstractrepositoryconfigurationsourcesupport)

Repository 설정 정보 캡슐화
- [RepositoryConfigurationSource](#repositoryconfigurationsource)
- [RepositoryConfigurationSourceSupport](#repositoryconfigurationsourcesupport)
- [AnnotationRepositoryConfigurationSource](#annotationrepositoryconfigurationsource)
- [AutoConfiguredAnnotationRepositoryConfigurationSource](#autoconfiguredannotationrepositoryconfigurationsource)

Repository 스캐닝 및 컨텍스트 등록
- [RepositoryConfigurationDelegate](#repositoryconfigurationdelegate)

스프링 데이터 Repository 설정

### AbstractRepositoryConfigurationSourceSupport

AbstractRepositoryConfigurationSourceSupport는 스프링 데이터에서 리포지토리 설정 및 공통 로직을 정의한 추상 클래스로

스프링 데이터의 리포지토리 설정 추상화/캡슐화 및 리포지토리 설정/스캐닝/등록 작업의 시작점임

스프링 데이터 JPA 뿐만 아니라 MongoDB, Cassandra 등 다양한 스프링 데이터 모듈의 리포지토리에서 공통적으로 사용되며, 각 데이터 소스 별로 구체적인 동작이 오버라이딩됨

스프링 데이터 JPA에서는 JpaRepositoriesRegistrar가 상속하여 스프링 데이터 JPA 기반 리포지토리가 동작할 수 있도록 설정 정보를 제공함

```java
/*
    AbstractRepositoryConfigurationSourceSupport가 구현하는 인터페이스 목록
    
    ImportBeanDefinitionRegistrar
    - 동적으로 빈 정의를 할 수 있는 인터페이스
    - 리포지토리 관련 빈을 등록하는데 사용됨
    
    BeanFactoryAware: 스프링 컨텍스트에 존재하는 빈 조회 또는 조작
    ResourceLoaderAware: 리소스 파일 로딩
    EnvironmentAware: 프로퍼티 파일 등 프로퍼티 소스에 접근
 */
public abstract class AbstractRepositoryConfigurationSourceSupport
		implements ImportBeanDefinitionRegistrar, BeanFactoryAware, ResourceLoaderAware, EnvironmentAware {

	private ResourceLoader resourceLoader;

	private BeanFactory beanFactory;

	private Environment environment;
    
    /*
        파라미터
        
        AnnotationMetadata: 이 메서드에서 사용 안함
        
        BeanDefinitionRegistry
        - 스프링 컨텍스트에 등록된 모든 빈 정보를 관리하는 인터페이스
        - 새로운 빈을 정의할 수 있기도 함
        - delegate.registerRepositoriesIn 메서드에 전달함(해당 메서드에서 사용됨)
        
        BeanNameGenerator: 빈 이름을 생성하는 데 사용되는 인터페이스 
    */

    // 구현체(JpaRepositoriesRegistrar)의 설정 정보를 바탕으로 리포지토리 인터페이스를 스캔하여 스프링 컨텍스트에 등록하는 메서드
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
			BeanNameGenerator importBeanNameGenerator) {
        /*
            getConfigurationSource(...)
            - 리포지토리 관련 설정 및 리포지토리 인터페이스 스캔 대상 패키지 목록 등에 대한 정보를 제공하는 객체 생성
            - AnnotationRepositoryConfigurationSource 타입(어노테이션 기반 리포지토리 설정 정보 제공)
            - 스프링 데이터 JPA의 경우 @EnableJpaRepositories 등에 대한 정보를 제공함
            
            new RepositoryConfigurationDelegate(...)
            - 리포지토리를 실제로 등록하는 작업을 수행하는 객체

         */
		RepositoryConfigurationDelegate delegate = new RepositoryConfigurationDelegate(
				getConfigurationSource(registry, importBeanNameGenerator), this.resourceLoader, this.environment);
        
        /*
            delegate.registerRepositoriesIn(...)
            - 리포지토리 인터페이스 스캔 및 스프링 컨텍스트에 등록
         */
		delegate.registerRepositoriesIn(registry, getRepositoryConfigurationExtension());
	}

    // BeanNameGenerator가 없는 경우를 대비한 오버로딩
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		registerBeanDefinitions(importingClassMetadata, registry, null);
	}

    /*
        getConfiguration 메서드에서 반환한 설정 클래스(@EnableJpaRepositories)를 기반으로
        리포지토리 설정 소스(AnnotationRepositoryConfigurationSource)를 생성함
     */
	private AnnotationRepositoryConfigurationSource getConfigurationSource(BeanDefinitionRegistry registry,
			BeanNameGenerator importBeanNameGenerator) {
		AnnotationMetadata metadata = AnnotationMetadata.introspect(getConfiguration());
		return new AutoConfiguredAnnotationRepositoryConfigurationSource(metadata, getAnnotation(), this.resourceLoader,
				this.environment, registry, importBeanNameGenerator) {
		};
	}

    // @SpringBootApplication 어노테이션이 위치한 패키지 반환
	protected Streamable<String> getBasePackages() {
		return Streamable.of(AutoConfigurationPackages.get(this.beanFactory));
	}

    // 리포지토리를 활성화하는 어노테이션 반환 (스프링 데이터 JPA: @EnableJpaRepositories)
	protected abstract Class<? extends Annotation> getAnnotation();

    // 리포지토리 설정 클래스 반환 (스프링 데이터 JPA: JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration)
	protected abstract Class<?> getConfiguration();
    
    // 리포지토리 설정 확장 클래스 반환 (스프링 데이터 JPA: JpaRepositoryConfigExtension)
	protected abstract RepositoryConfigurationExtension getRepositoryConfigurationExtension();
    
	protected BootstrapMode getBootstrapMode() {
		return BootstrapMode.DEFAULT;
	}
    
    // 리포지토리 스캔 및 부트스트랩 정보 캡슐화
	private class AutoConfiguredAnnotationRepositoryConfigurationSource
			extends AnnotationRepositoryConfigurationSource {

		AutoConfiguredAnnotationRepositoryConfigurationSource(AnnotationMetadata metadata,
				Class<? extends Annotation> annotation, ResourceLoader resourceLoader, Environment environment,
				BeanDefinitionRegistry registry, BeanNameGenerator generator) {
			super(metadata, annotation, resourceLoader, environment, registry, generator);
		}

        // @SpringBootApplication 어노테이션이 위치한 패키지 반환
		@Override
		public Streamable<String> getBasePackages() {
			return AbstractRepositoryConfigurationSourceSupport.this.getBasePackages();
		}

        // Bootstrap.DEFAULT 반환
		@Override
		public BootstrapMode getBootstrapMode() {
			return AbstractRepositoryConfigurationSourceSupport.this.getBootstrapMode();
		}

	}

}
```

## Repository 설정 정보 캡슐화

### RepositoryConfigurationSource

스프링 데이터 리포지토리(JPA, MongoDB, Cassandra 등)에 대한 설정 정보를 추상화하여 제공하는 인터페이스임

#### 리포지토리 설정과 관련된 메타데이터 제공

`@EnableJpaRepositories` 등 리포지토리 활성화 어노테이션을 기반으로 리포지토리 인터페이스의 패키지를 스캔하여 해당 정보를 제공하거나

Query 구현, 커스텀 리포지토리 스캔 및 설정하기 위한 정보를 추출하여 저장함

#### 스프링 데이터 기본 동작 커스텀

QueryLookupStrategy, RepositoryFactoryBean 설정 등을 통해 스프링 데이터 기본 동작을 커스텀할 수 있음

```java
public interface RepositoryConfigurationSource {

    /* =============== 리포지토리 스캔 관련 =============== */
    
    /*
        리포지토리 스캔 대상 패키지 목록 반환
        - 스프링 데이터 JPA: @EnableJpaRepositories의 basePackages, basePackageClasses 
    */
	Streamable<String> getBasePackages();

    /*
        스캔으로 찾은 리포지토리 인터페이스 목록 반환
        각 리포지토리는 BeanDefinition 형태로 반환됨
     */
    Streamable<BeanDefinition> getCandidates(ResourceLoader loader);

    // 리포지토리 스캔 시 명시적으로 정의된 필터 사용 여부 반환
    boolean usesExplicitFilters();

    // 리포지토리 스캔 시 적용할 제외 필터 반환
    Streamable<TypeFilter> getExcludeFilters();
    
    
    /* =============== 커스텀 리포지토리 관련 =============== */
    
    /*
        커스텀 리포지토리 구현체 접미사 반환
        기본값 : Impl (UserRepository의 구현체 -> UserRepositoryImpl)
     */
	Optional<String> getRepositoryImplementationPostfix();

    // 커스텀 리포지토리 구현체를 검색하기 위한 설정 정보 반환
    ImplementationDetectionConfiguration toImplementationDetectionConfiguration(MetadataReaderFactory factory);

    
    /* =============== 쿼리 관련 =============== */

    /*
        스프링 데이터 Query 생성 전략 반환
        QueryLookupStrategy: @Query 어노테이션, 메서드 이름 기반 쿼리 생성 시 사용할 전략
        
        QueryLookupStrategyKey 값
        - CREATE: 쿼리 생성
        - USE_DECLARED_QUERY: 선언된 쿼리만 사용
        - CREATE_IF_NOT_FOUND: 선언된 쿼리가 없으면 생성
     */
    Optional<Object> getQueryLookupStrategyKey();
    
    // Named Query 위치 반환
	Optional<String> getNamedQueryLocation();
    

    /* =============== 리포지토리 구성 클래스 관련 =============== */
    
    /*
        리포지토리 기본 클래스 이름 반환
        스프링 데이터 JPA: SimpleJpaRepository 
     */
	Optional<String> getRepositoryBaseClassName();

    /*
        리포지토리 팩토리 빈 클래스 이름 반환
        기본값: RepositoryFactoryBean (스프링 데이터 제공)
     */
	Optional<String> getRepositoryFactoryBeanClassName();


    /* =============== 기타 설정 =============== */
    
    // 리포지토리 설정 추가 속성 반환
	Optional<String> getAttribute(String name);
	<T> Optional<T> getAttribute(String name, Class<T> type);

    /*
        리포지토리 빈 부트스트랩 모드 반환
        - DEFAULT: 기본 부트스트랩
        - LAZY: 지연 초기화
        - DEFERRED: 컨텍스트가 완전히 초기화된 후 빈 생성
     */
    BootstrapMode getBootstrapMode();

    @Nullable
    String getResourceDescription();

    
    /* =============== 기타 설정 =============== */
    
    // 리포지토리 인터페이스로부터 생성된 스프링 빈 이름 반환
	String generateBeanName(BeanDefinition beanDefinition);
	
}
```

### RepositoryConfigurationSourceSupport

RepositoryConfigurationSource 어노테이션 기반, XML 기반 클래스 등의 기본 기능과 구조를 제공하는 추상 클래스

주요 역할
- 리포지토리 스캔 및 설정 처리 관련된 로직 공통화
- 설정 정보 초기화 및 관리
- 스프링 빈 등록시 필요한 기본 도구(Environment, ResourceLoader, BeanNameGenerator) 관리
- 커스텀 리포지토리 구현체 접미사 기본값(`Impl`) 보유

```java
public abstract class RepositoryConfigurationSourceSupport implements RepositoryConfigurationSource {

    // 커스텀 리포지토리 접미사 기본값
    protected static final String DEFAULT_REPOSITORY_IMPL_POSTFIX = "Impl";

    
    /* =============== 생성자 =============== */
    
    // 빈 등록시 필요한 도구 관리
    public RepositoryConfigurationSourceSupport(Environment environment, ClassLoader classLoader,
                                                BeanDefinitionRegistry registry, BeanNameGenerator generator) {

        this.environment = environment;
        // 리포지토리 빈 이름 생성기
        this.beanNameGenerator = new RepositoryBeanNameGenerator(classLoader, generator, registry);
        this.registry = registry;
    }

    /*=============== 리포지토리 인터페이스 목록 스캔 =================*/
    
    /*
        스캔 결과로 찾은 리포지토리 인터페이스 목록을 반환하는 메서드
        각 목록은 BeanDefinition 타입으로 반환됨
     */
    @Override
    public Streamable<BeanDefinition> getCandidates(ResourceLoader loader) {

        // 베이스 패키지를 기준으로 리포지토리 인터페이스를 스캔하는 객체 생성
        RepositoryComponentProvider scanner = new RepositoryComponentProvider(getIncludeFilters(), registry);
        
        // 스캐너 설정
        scanner.setConsiderNestedRepositoryInterfaces(shouldConsiderNestedRepositories());
        scanner.setEnvironment(environment);
        scanner.setResourceLoader(loader);

        getExcludeFilters().forEach(scanner::addExcludeFilter);

        /*
            getBasePackages 메서드는 리포지토리 인터페이스 스캔 대상 패키지 목록을 반환하는 추상 메서드임
            각 패키지(it)에 대해 스캐너를 통해 해당 패키지에서 조건에 맞는 리포지토리 인터페이스를 탐색함
         */
        return Streamable.of(() -> getBasePackages().stream()//
                .flatMap(it -> scanner.findCandidateComponents(it).stream()));
    }

    /*================ 리포지토리 빈 이름 설정 =========== */
    
    @Override
    public String generateBeanName(BeanDefinition beanDefinition) {
        return beanNameGenerator.generateBeanName(beanDefinition);
    }

    /*================ 커스텀 리포지토리 구현체를 검색하기 위한 설정 정보 반환 ============== */
    
    @Override
    public ImplementationDetectionConfiguration toImplementationDetectionConfiguration(MetadataReaderFactory factory) {
        return new SpringImplementationDetectionConfiguration(this, factory);
    }

    /*
        커스텀 리포지토리 구현체 감지 구현체, toImplementationDetectionConfiguration에서 생성
        대부분 현재 인스턴스의 정보를 설정 정보로 반환함
     */
    private class SpringImplementationDetectionConfiguration implements ImplementationDetectionConfiguration {

        private final RepositoryConfigurationSource source;
        private final MetadataReaderFactory metadataReaderFactory;
        
        SpringImplementationDetectionConfiguration(RepositoryConfigurationSource source,
                                                   MetadataReaderFactory metadataReaderFactory) {
            this.source = source;
            this.metadataReaderFactory = metadataReaderFactory;
        }

        /*
            커스텀 리포지토리 구현체의 접미사를 별도로 설정한 경우, 그 값을 쓰고
            아닌 경우 기본값인 "Impl" 사용
         */
        @Override
        public String getImplementationPostfix() {
            return source.getRepositoryImplementationPostfix()
                    .orElse(DefaultRepositoryConfiguration.DEFAULT_REPOSITORY_IMPLEMENTATION_POSTFIX);
        }

        @Override
        public Streamable<String> getBasePackages() {
            return source.getBasePackages();
        }

        @Override
        public Streamable<TypeFilter> getExcludeFilters() {
            return source.getExcludeFilters();
        }

        @Override
        public String generateBeanName(BeanDefinition definition) {
            return source.generateBeanName(definition);
        }

        @Override
        public MetadataReaderFactory getMetadataReaderFactory() {
            return this.metadataReaderFactory;
        }
    }
}
```

### AnnotationRepositoryConfigurationSource

어노테이션 기반 리포지토리 설정 정보를 제공하는 [RepositoryConfigurationSourceSupport](#repositoryconfigurationsourcesupport) 구현체

스프링 데이터의 `@Enable*Repositories` 어노테이션(`EnableJpaRepositories` `@EnableMongoRepositories`) 등을 처리하며, 이를 기반으로 리포지토리 인터페이스를 스캔하고 설정 정보를 제공함

```java
public class AnnotationRepositoryConfigurationSource extends RepositoryConfigurationSourceSupport {

    /*================== 필드(문자열 상수 필드 제외) ===================*/
    
    /*
        @Enable*Repositories 어노테이션이 선언된 클래스의 메타데이터
        리포지토리 스캐닝 설정의 시작점으로 사용됨
        리포지토리 베이스 패키지, 커스텀 구현, 필터 등을 정의한 정보를 가져옴
     */
    private final AnnotationMetadata configMetadata; 
            
    /*
        @Enable*Repositories 어노테이션의 메타데이터 저장
        커스텀 리포지토리 접미사(Impl), 쿼리 조회 전략, 커스텀 리포지토리 팩토리 클래스 등 결정
     */
    private final AnnotationMetadata enableAnnotationMetadata;

    // Enable*Repositories 어노테이션 속성값 저장
    private final AnnotationAttributes attributes;

    // 어노테이션 속성을 기반으로 TypeFilter를 생성하는 함수형 인터페이스
    private final Function<AnnotationAttributes, Stream<TypeFilter>> typeFilterFunction;
    
    // 명시적으로 필터가 설정된지 나타냄, 기본 스캔 동작과 커스텀 스캔 동작 구분 목적
    private final boolean hasExplicitFilters;
    
    /*================= 리포지토리 인터페이스 스캔 대상 패키지 목록 조회 ==================
     *
     * @Enable@Repositories 어노테이션의 설정을 기반으로 리포지토리 스캔 대상 패키지 목록을 반환함
     * 스프링 데이터 리포지토리 초기화 과정에서 호출됨
     */
    @Override
    public Streamable<String> getBasePackages() {

        String[] value = attributes.getStringArray("value");
        String[] basePackages = attributes.getStringArray(BASE_PACKAGES);
        Class<?>[] basePackageClasses = attributes.getClassArray(BASE_PACKAGE_CLASSES);
        
        // 하나의 패키지도 명시되지 않은 경우 @Enable*Repositories 어노테이션 선언 위치가 리포지토리 스캔 대상 패키지가 됨
        if (value.length == 0 && basePackages.length == 0 && basePackageClasses.length == 0) {

            String className = configMetadata.getClassName();
            return Streamable.of(ClassUtils.getPackageName(className));
        }

        /*
            @Enable*Repositories 어노테이션에 한 개 이상의 패키지가 명시된 경우
            지정된 패키지들을 기반으로 베이스 패키지 목록을 구성함
         */
        Set<String> packages = new HashSet<>(value.length + basePackages.length + basePackageClasses.length);
        packages.addAll(Arrays.asList(value));
        packages.addAll(Arrays.asList(basePackages));

        for (Class<?> c : basePackageClasses) {
            packages.add(ClassUtils.getPackageName(c));
        }

        return Streamable.of(packages);
    }
    
    /*
        쿼리 전략, Named Query 위치, 커스텀 리포지토리 구현체 접미사 등의 설정 정보는
        @Enable*Repositories에 명시된 속성값을 기반으로 반환하므로 생략
     */
}
```

### AutoConfiguredAnnotationRepositoryConfigurationSource

스프링 데이터 하위 모듈(JPA, MongoDB 등)의 리포지토리 인터페이스 자동 등록 과정(JpaRepositoriesRegistrar, MongoRepositoriesRegistrar 등)은 모두 AbstractRepositoryConfigurationSourceSupport를 상속받아서 구현되는데, 

AutoConfiguredAnnotationRepositoryConfigurationSource는 이 클래스 내부에서 사용되는 구현체로 어노테이션을 기반으로 리포지토리 인터페이스를 스캔하고 리포지토리 설정 정보를 제공함

```java
private class AutoConfiguredAnnotationRepositoryConfigurationSource
			extends AnnotationRepositoryConfigurationSource {

    AutoConfiguredAnnotationRepositoryConfigurationSource(AnnotationMetadata metadata,
            Class<? extends Annotation> annotation, ResourceLoader resourceLoader, Environment environment,
            BeanDefinitionRegistry registry, BeanNameGenerator generator) {
        super(metadata, annotation, resourceLoader, environment, registry, generator);
    }

    // @SpringBootApplication 어노테이션이 위치한 패키지 반환
    @Override
    public Streamable<String> getBasePackages() {
        return AbstractRepositoryConfigurationSourceSupport.this.getBasePackages();
    }

    // Bootstrap.DEFAULT 반환
    @Override
    public BootstrapMode getBootstrapMode() {
        return AbstractRepositoryConfigurationSourceSupport.this.getBootstrapMode();
    }

}
```

## Repository 스캐닝 및 컨텍스트 등록

### RepositoryConfigurationDelegate

