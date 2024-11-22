[HibernateJpaAutoConfiguration](#hibernatejpaautoconfiguration)
- [코드 분석](#코드-분석)

[JpaBaseConfiguration](#jpabaseconfiguration)

[HibernateJpaConfiguration](#hibernatejpaconfiguration)

[JpaRepositoriesAutoConfiguration](#jparepositoriesautoconfiguration)

## HibernateJpaAutoConfiguration

HibernateJpaAutoConfiguration는 JPA와 하이버네이트 설정을 처리하고 EntityManagerFactory, TransactionManager 등을 자동 구성하는 클래스임

### 코드 분석

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
- 스프링 프레임워크에서 JPA의 `EntityManagerFactory`를 스프링 빈으로 등록하는 데 사용된 클래스임
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

[HibernateJpaConfiguration](#hibernatejpaconfiguration)

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

## JpaBaseConfiguration

스프링 부트에서 특정 JPA 구현체를 떠나 JPA와 관련된 공통 설정을 제공하는 @Configuration 추상 클래스임

JPA 빈 정의
- PlatformTransactionManager
- EntityManagerFactoryBuilder
- EntityManagerFactory
- LocalContainerEntityManagerFactoryBean
- JpaVendorAdapter
- PersistenceManagedTypes
- OpenEntityManagerInViewInterceptor
- WebMvcConfigurer

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

```java
@Bean

// 트랜잭션 매니저 타입의 빈이 등록되지 않은 경우(사용자가 직접 정의하지 않은 경우)
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

JpaBaseConfiguration 클래스는 생성자를 통해 JtaTransactionManager를 주입받는데, 이 클래스는 TransactionManager의 구현체임

JTA 환경인 경우에는 이미 TransactionaManager 빈이 등록되어 있는 상황이므로 JpaTransactionManager가 빈으로 등록되지 않음

스프링 부트가 다양한 트랜잭션 환경(JTA, JPA)에서 동작하는 유연성을 제공하는 것을 볼 수 있음

##### JpaVendorAdapter

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

JpaVendorAdapter는 JPA 구현체에게 종속적인 설정을 추상화한 어댑터 인터페이스로

Hibernate, EclipseLink 등의 구현체와의 호환성 지원, JPA 속성 및 Dialect 설정 기능을 제공함

##### EntityManagerFactoryBuilder

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

EntityManagerFactoryBuilder는 EntityManagerFactory를 생성하기 위한 유틸 클래스임

JpaVendorAdapter, 데이터 소스, JPA 프로퍼티 등을 종합하여 EntityManagerFactory를 구성함

##### LocalContainerEntityManagerFactoryBean

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
```

LocalContainerEntityManagerFactoryBean은 EntityManagerFactory를 실제로 생성하는 객체임



## HibernateJpaConfiguration


```java
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HibernateProperties.class)
@ConditionalOnSingleCandidate(DataSource.class)
@ImportRuntimeHints(HibernateRuntimeHints.class)
class HibernateJpaConfiguration extends JpaBaseConfiguration {

}
```

HibernateJpaConfiguration 클래스는 JpaBaseConfiguration을 상속받아 하이버네이트를 사용할 때 필요한 세부적인 설정을 수행함
- LocalContainerEntityManagerFactoryBean, PlatformTransactionManager, EntityManagerFactory, JpaVendorAdapter 등 빈 정의 (JpaBaseConfiguration 기본 설정 상속)
- 하이버네이트 관련 커스터마이징 및 프로퍼티 설정 `spring.jpa.properties.hibernate.*

## JpaRepositoriesAutoConfiguration