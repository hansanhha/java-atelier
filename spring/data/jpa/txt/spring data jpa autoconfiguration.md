[HibernateJpaAutoConfiguration](#hibernatejpaautoconfiguration)
- [코드 분석](#코드-분석)

[JpaBaseConfiguration](#jpabaseconfiguration)

[HibernateJpaConfiguration](#hibernatejpaconfiguration)

[JpaRepositoriesAutoConfiguration](#jparepositoriesautoconfiguration)

## HibernateJpaAutoConfiguration

HibernateJpaAutoConfiguration는 JPA와 하이버네이트 설정을 처리하고 EntityManagerFactory, TransactionManager 등을 자동 구성하는 클래스임

### 코드 분석

#### 전체 코드

```java
@AutoConfiguration(
        after = { DataSourceAutoConfiguration.class, TransactionManagerCustomizationAutoConfiguration.class },
        before = { TransactionAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })
@ConditionalOnClass({ LocalContainerEntityManagerFactoryBean.class, EntityManager.class, SessionImplementor.class })
@EnableConfigurationProperties(JpaProperties.class)
@Import(HibernateJpaConfiguration.class)
public class HibernateJpaAutoConfiguration {

}
```

#### @AutoConfiguration

```java
@AutoConfiguration(
        after = { DataSourceAutoConfiguration.class, TransactionManagerCustomizationAutoConfiguration.class },
        before = { TransactionAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })
```

`@AutoConfiguration` 어노테이션은 스프링 부트 애플리케이션이 구동되면서 컨텍스트를 초기화는 과정에서 자동 구성을 시도하는 클래스임을 나타냄

before/after 속성은 어노테이션이 선언된 자동 구성 클래스 이전/이후에 수행되어야 할 자동 구성 클래스를 지정함

전처리 자동 구성 클래스
- **TransactionAutoConfiguration**
    - 스프링의 트랜잭션 관리 관련 기본 설정 수행
    - JPA 설정 전, 트랜잭션 관련 구성 필요
- **DataSourceTransactionManagerAutoConfiguration**
    - JDBC 기반 트랜잭션 관리자 구성 수행
    - JPA 설정 전, 데이터 소스 기반 트랜잭션 관리자 설정 필요

후처리 자동 구성 클래스
- **DataSourceAutoConfiguration**
    - 데이터 소스 자동 구성
    - 임베디드 데이터베이스(H2) 또는 커넥션 풀 데이터 소스(HikaryCP 등)
- **TransactionManagerCustomizationAutoConfiguration**
    - 트랜잭션 관리자와 관련된 커스텀 설정 처리
    - 기본 트랜잭션 관리자 확장 또는 수정

#### @ConditionalOnClass

```java
@ConditionalOnClass({ LocalContainerEntityManagerFactoryBean.class, EntityManager.class, SessionImplementor.class })
```

`@ConditionalOnClass` 어노테이션은 클래스패스에 특정 클래스가 존재할 경우(라이브러리 탐지) 조건에 매치되는 것으로 판단함

속성 값에 포함된 리스트는 JPA 및 하이버네이트와 관련된 클래스들로, 클래스패스에 하이버네이트와 JPA 및 스프링 orm 모듈이 있는지 필터링함

**LocalContainerEntityManagerFactoryBean**
- 스프링 프레임워크에서 JPA의 `EntityManagerFactory`를 스프링 빈으로 등록하는 데 사용되는 클래스임
- JPA 구현체(하이버네이트 등)를 초기화하고 스프링 컨텍스트와 통합함
- `spring-orm`

**EntityManager**
- 엔티티 영속성 관리 인터페이스
- 데이터베이스 상호작용(CRUD, JPQL, 트랜잭션 관리 등)
- `jakarta.persistence`

**SessionImplementor**
- 하이버네이트의 Session 확장 인터페이스
- `org.hibernate`

#### @EnableConfigurationProperties

```java
@EnableConfigurationProperties(JpaProperties.class)
```

`@EnableConfigurationProperties` 어노테이션은 `@ConfigurationProperties` 클래스를 활성화하고, 스프링 컨텍스트에 빈으로 등록함

```java
@ConfigurationProperties(prefix = "spring.jpa")
public class JpaProperties {
    ...
}
```

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

JpaRepositories 클래스는 `spring.jpa`로 시작하는 모든 프로퍼티를 매핑함

스프링 빈으로 등록된 JpaRepositories는 이후 JPA 관련 구성에 주입되어 매핑된 프로퍼티들을 제공함

#### @Import

```java
@Import(HibernateJpaConfiguration.class)
```

`@Import` 어노테이션은 특정 클래스를 스프링 컨텍스트에 등록함
- 해당 클래스는 `@Configuration` 클래스로 간주되어, 추가적인 설정 및 빈 정의가 컨텍스트에 포함됨

HibernateJpaAutoConfiguration은 @AutoConfiguration을 통해 스프링의 트랜잭션 관련 설정을 마치고, 클래스패스에 JPA 및 하이버네이트 라이브러리가 탐지되면

JPA 프로퍼티 클래스와 Hibernate JPA 구성 클래스를 스프링 빈으로 등록함

[HibernateJpaConfiguration](#hibernatejpaconfiguration)

## JpaBaseConfiguration

스프링 부트에서 특정 JPA 구현체를 떠나 JPA와 관련된 공통 설정을 제공하는 추상 @Configuration 클래스임

JpaBaseConfiguration에서 정의하는 JPA 빈
- PlatformTransactionManager: 트랜잭션 관리
- JpaVendorAdapter: JPA 구현체 종속 설정 추상화
- EntityManagerFactoryBuilder: EntityManagerFactory 생성 유틸 클래스 (LocalContainerEntityManagerFactoryBean의 빌더 역할)
- LocalContainerEntityManagerFactoryBean: 스프링 환경에서 EntityManagerFactory 생성 및 관리 
- PersistenceManagedTypes: JPA 애플리케이션에서 관리할 모든 엔티티(또는 JPA 관련 클래스), 패키지 추적 및 구성
- OpenEntityManagerInViewInterceptor: Open In Session View(OSIV) 패턴 활성화
- WebMvcConfigurer: InterceptorRegistry에 OpenEntityManagerInViewInterceptor 추가

#### 필드 및 생성자

```java
public abstract class JpaBaseConfiguration {

    private final DataSource dataSource; // 데이터소스
    private final JpaProperties properties; // JPA 프로퍼티
    private final JtaTransactionManager jtaTransactionManager; // JTA(Java Transaction API) 기반 트랜잭션 관리자

    protected JpaBaseConfiguration(DataSource dataSource, JpaProperties properties,
                                   ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
        this.dataSource = dataSource;
        this.properties = properties;
        this.jtaTransactionManager = jtaTransactionManager.getIfAvailable();
    }
}
```

#### 빈 정의

##### PlatformTransactionManager

PlatformTransactionManager는 스프링 프레임워크에서 트랜잭션 관리(트랜잭션 시작, 커밋, 롤백)를 담당하는 인터페이스로 메시징, JPA 같은 여러 트랜잭션 리소스를 일관적으로 처리하도록 추상화함

JpaBaseConfiguration 클래스는 생성자를 통해 JtaTransactionManager를 주입받는데, JpaTrasactionManager와 마찬가지로 TransactionManager의 구현체임

JTA 환경인 경우에는 이미 TransactionaManager 빈이 등록되어 있는 상황이므로 JpaTransactionManager가 빈으로 등록되지 않음

스프링 부트가 다양한 트랜잭션 환경(JTA, JPA)에서 동작하는 유연성을 제공하는 것을 볼 수 있음

```java
@Bean

/*
    트랜잭션 매니저 타입의 빈이 등록되지 않은 경우(사용자가 직접 정의하지 않은 경우)
    PlatformTransactionManager 구현체로 JpaTransactionManager를 생성하고
    파라미터로 주입받은 트랜잭션 커스터마이징 적용(optional)
 */
@ConditionalOnMissingBean(TransactionManager.class)
public PlatformTransactionManager transactionManager(
        ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
    /*
        ObjectProvider: 지연 로딩 또는 조건부 주입 기능 제공
        TransactionManagerCustomizers 타입의 빈이 컨텍스트에 존재할 경우 커스터마이징하도록 조건을 제공함
     */


    // JpaTransactionManager(PlatformTransactionManager 구현체) 생성
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    
    /*
        TransactionManagerCustomizers는 스프링 부트 자동 구성 기능에 의해 생성되지 않음
        개발자가 별도로 트랜잭션 매니저에 대한 커스텀이 필요한 경우 스프링 빈으로 등록하면
        스프링 부트는 커스터마이징 빈을 가져와서 커스텀 로직을 적용함
     */
    transactionManagerCustomizers
            .ifAvailable((customizers) -> customizers.customize((TransactionManager) transactionManager));
    return transactionManager;
}
```

##### JpaVendorAdapter

JpaVendorAdapter는 JPA 구현체에게 종속적인 설정을 추상화한 어댑터 인터페이스로

Hibernate, EclipseLink 등의 구현체와의 호환성 지원, JPA 속성 및 Dialect 설정 기능을 제공함

```java
@Bean
@ConditionalOnMissingBean // JpaVendorAdapter 타입의 빈이 컨텍스트에 등록되지 않은 경우
public JpaVendorAdapter jpaVendorAdapter() {

    // 템플릿 메서드를 통해 JpaBaseConfiguration 구현체에게 AbstractJpaVendorAdapter 생성 위임
    AbstractJpaVendorAdapter adapter = createJpaVendorAdapter();

    // JpaBaseConfiguration 생성 시 주입받은 JpaProperties를 통해 사용자가 설정한 프로퍼티 값을 적용
    adapter.setShowSql(this.properties.isShowSql());
    if (this.properties.getDatabase() != null) {
        adapter.setDatabase(this.properties.getDatabase());
    }
    if (this.properties.getDatabasePlatform() != null) {
        adapter.setDatabasePlatform(this.properties.getDatabasePlatform());
    }
    adapter.setGenerateDdl(this.properties.isGenerateDdl());
    return adapter;
}

// AbstractJpaVendorAdapter 생성 위임
protected abstract AbstractJpaVendorAdapter createJpaVendorAdapter();
```

##### EntityManagerFactoryBuilder

EntityManagerFactoryBuilder는 EntityManagerFactory를 생성하기 위한 유틸 클래스임

JpaVendorAdapter, 데이터 소스, JPA 프로퍼티 등을 종합하여 EntityManagerFactory를 구성함

```java
@Bean
@ConditionalOnMissingBean // EntityManagerFactoryBuilder 타입의 빈이 컨텍스트에 등록되지 않은 경우
public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter,
                                                               ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
                                                               ObjectProvider<EntityManagerFactoryBuilderCustomizer> customizers) {
    /*
        ObjectProvider: 지연 로딩 또는 조건부 주입 기능 제공
        PersistenceUnitManager
            - PersistenceUnit 관리 역할
            - PersistenceUnit에 대한 추가적인 설정이나, 여러 데이터 소스를 사용하는 경우(JPA 엔티티들이 속한 데이터베이스 연결 설정 정의 등)
        EntityManagerFactoryBuilderCustomizer: EntityManagerFactoryBuilder 커스터마이징
     */

    
    EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter,
            this.properties.getProperties(), persistenceUnitManager.getIfAvailable());
    
    customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
    return builder;
}
```

##### LocalContainerEntityManagerFactoryBean

LocalContainerEntityManagerFactoryBean은 스프링 환경에서 EntityManagerFactory를 생성하고 관리하는 역할을 함

```java
@Bean
@Primary
@ConditionalOnMissingBean({ LocalContainerEntityManagerFactoryBean.class, EntityManagerFactory.class })
public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factoryBuilder,
        PersistenceManagedTypes persistenceManagedTypes) {
    
    // 템플릿 메서드를 통해 JPA 구현체마다 설정된 프로퍼티 값을 가져옴
    Map<String, Object> vendorProperties = getVendorProperties();
    
    // JPA 구현체에 맞는 프로퍼티 커스터마이징
    customizeVendorProperties(vendorProperties);
    
    // 위에서 등록한 EntityManagerFactoryBuilder를 통해 LocalContainerEntityManagerFactoryBean 생성
    return factoryBuilder.dataSource(this.dataSource)
        .managedTypes(persistenceManagedTypes)
        .properties(vendorProperties)
        .mappingResources(getMappingResources())
        .jta(isJta())
        .build();
    
}

// 각 JPA 구현체에 맞는 프로퍼티 반환
protected abstract Map<String, Object> getVendorProperties();

// 각 JPA 구현체에 맞는 프로퍼티 커스터마이징(옵션)
protected void customizeVendorProperties(Map<String, Object> vendorProperties) {
}

private String[] getMappingResources() {
  List<String> mappingResources = this.properties.getMappingResources();
  return (!ObjectUtils.isEmpty(mappingResources) ? StringUtils.toStringArray(mappingResources) : null);
}

protected final boolean isJta() {
  return (this.jtaTransactionManager != null);
}
```

##### PersistenceManagedTypes

PersistenceManagedTypes는 스프링 데이터 JPA 내부 클래스로, JPA 애플리케이션에서 관리할 모든 엔티티(또는 JPA 관련 클래스), 패키지들을 추적하고 구성함

스프링 데이터 JPA는 PersistenceManagedTypes를 통해 특정 컨텍스트에서 JPA 엔티티를 스캔하여 관리해야 할 엔티티 목록을 구성함

```java
@Configuration(proxyBeanMethods = false)

/*
    PersistenceManagedTypesConfiguration 클래스는 LocalContainerEntityManagerFactoryBean 빈 등록 이전에 평가됨
    LocalContainerEntityManagerFactoryBean와 EntityManagerFactory 빈이 없는 경우(다른 방식으로 JPA 설정을 하지 않은 경우)
    
    스캔 대상 패키지 결정 후, 엔티티 스캔 -> 스캔 결과(엔티티 클래스 및 패키지)를 포함한 PersistenceManagedTypes 객체 생성 및 빈 등록
 */
@ConditionalOnMissingBean({ LocalContainerEntityManagerFactoryBean.class, EntityManagerFactory.class })
static class PersistenceManagedTypesConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    static PersistenceManagedTypes persistenceManagedTypes(BeanFactory beanFactory, ResourceLoader resourceLoader,
            ObjectProvider<ManagedClassNameFilter> managedClassNameFilter) {
        /*
            BeanFactory
            - 스프링 빈 컨테이너 최상위 인터페이스
            - EntityScanPackages 및 AutoConfigurationPackages를 사용해 스캔할 패키지 목록을 가져옴
            
            ResourceLoader
            - 클래스패스 및 파일 시스템에서 리소스를 읽어옴
            - 엔티티 스캔에 활용
            
            ObjectProvider<ManagedClassNameFilter>
            - ObjectProvider: 지연 로딩 또는 조건부 주입 기능 제공
            - ManagedClassNameFilter: 특정 클래스 이름 필터링
         */
      
        // 스캔할 패키지 목록을 가져옴
        String[] packagesToScan = getPackagesToScan(beanFactory);
        
        /*
            PersistenceManagedTypesScanner를 생성해서 해당 패키지의 엔티티를 스캔
            스캔 결과를 포함한 PersistenceManagedTypes 객체 생성 및 반환
         */
        return new PersistenceManagedTypesScanner(resourceLoader, managedClassNameFilter.getIfAvailable())
            .scan(packagesToScan);
    }

    // 스캔 대상 패키지 결정 메서드
    private static String[] getPackagesToScan(BeanFactory beanFactory) {
        /*
            EntityScanPackages
            - @EntityScan 어노테이션을 통해 스캔할 엔티티 패키지를 저장하는 유틸 클래스
        
            EntityScanPackages.get 메서드
            - 현재 빈 팩토리에 EntityScanPackages 타입의 스프링 빈이 있는 경우(사용자의 별도 설정) 해당 빈 반환
            - 없으면 미리 생성되어 있는 EntityScanPackages 인스턴스 반환(기본 동작) 
         */
        List<String> packages = EntityScanPackages.get(beanFactory).getPackageNames();
        
        /*
            AutoConfigurationPackages
            - 스프링 부트가 자동으로 설정한 기본 패키지 경로를 관리하는 유틸 클래스
            - @SpringBootApplication 어노테이션이 위치한 패키지 및 하위 패키지 포함
         */
        if (packages.isEmpty() && AutoConfigurationPackages.has(beanFactory)) {
            // 스캔 대상 패키지를 가져옴
            packages = AutoConfigurationPackages.get(beanFactory);
        }
        return StringUtils.toStringArray(packages);
    }

}
```

##### OpenEntityManagerInViewInterceptor, WebMvcConfigurer

OpenEntityManagerInViewInterceptor는 스프링 Open EntityManager In View 패턴(OEMIV)을 구현하기 위한 인터셉터임

Open Session In View(OSIV)라고도 하는데, 이 패턴은 JPA 엔티티 관리를 트랜잭션 경계 바깥인 웹 요청의 시작부터 종료까지 유지하여 뷰 레이어에서도 엔티티에 대한 지연 로딩을 허용함

WebMvcConfigurer는 스프링 MVC 설정을 확장하거나 커스터마이징할 때 사용하는 인터페이스로, OpenEntityManagerInViewInterceptor를 InterceptorRegistry에 추가하기 위해 JpaBaseConfiguration 클래스에서 사용됨

```java
@Configuration(proxyBeanMethods = false)

// Servlet 환경인 경우
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass(WebMvcConfigurer.class)

// 스프링 컨텍스트에 OpenEntityManagerInView와 관련된 빈이 없는 경우
@ConditionalOnMissingBean({ OpenEntityManagerInViewInterceptor.class, OpenEntityManagerInViewFilter.class })
@ConditionalOnMissingFilterBean(OpenEntityManagerInViewFilter.class)

// spring.jpa.open-in-view 프로퍼티 값이 true이거나 설정하지 않은 경우
@ConditionalOnProperty(prefix = "spring.jpa", name = "open-in-view", havingValue = "true", matchIfMissing = true)
protected static class JpaWebConfiguration {

    private static final Log logger = LogFactory.getLog(JpaWebConfiguration.class);

    private final JpaProperties jpaProperties;

    protected JpaWebConfiguration(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    /*
        스프링 데이터 JPA는 기본적으로 사용자가 spring.jpa.open-in-view 프로퍼티 값을
        명시적으로 false로 설정하지 않으면 OSIV를 활성화함
        
        스프링 부트 애플리케이션을 시작할 때 나오는 경고문의 원인이기도 함
     */
    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
        if (this.jpaProperties.getOpenInView() == null) {
            logger.warn("spring.jpa.open-in-view is enabled by default. "
                    + "Therefore, database queries may be performed during view "
                    + "rendering. Explicitly configure spring.jpa.open-in-view to disable this warning");
        }
        return new OpenEntityManagerInViewInterceptor();
    }
    
    // OpenEntityManagerInViewInterceptor를 InterceptorRegistry에 추가함
    @Bean
    public WebMvcConfigurer openEntityManagerInViewInterceptorConfigurer(
            OpenEntityManagerInViewInterceptor interceptor) {
        return new WebMvcConfigurer() {

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addWebRequestInterceptor(interceptor);
            }

        };
    }

}
```

## HibernateJpaConfiguration

하이버네이트 JpaBaseConfiguration 구현체

```java
@Configuration(proxyBeanMethods = false)

// HibernateProperties 프로퍼티 클래스 컨텍스트 등록
@EnableConfigurationProperties(HibernateProperties.class)

// DataSource 빈이 하나이거나 @Primary로 지정된 경우
@ConditionalOnSingleCandidate(DataSource.class)
 
@ImportRuntimeHints(HibernateRuntimeHints.class)
class HibernateJpaConfiguration extends JpaBaseConfiguration {

}
```

HibernateJpaConfiguration 클래스는 JpaBaseConfiguration을 상속받아 하이버네이트를 사용할 때 필요한 세부적인 설정을 수행함
- LocalContainerEntityManagerFactoryBean, PlatformTransactionManager, EntityManagerFactory, JpaVendorAdapter 등 빈 정의 (JpaBaseConfiguration 기본 설정 상속)
- 하이버네이트 관련 커스터마이징 및 프로퍼티 설정 `spring.jpa.properties.hibernate.*

## JpaRepositoriesAutoConfiguration