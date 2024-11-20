[autoconfiguration 개요](#autoconfiguration-개요)

[spring.factories](#springfactories)

[@AutoConfiguration](#autoconfiguration)

[@EnableAutoConfiguration](#enableautoconfiguration)

[Spring Boot AutoConfiguration Workflow](#spring-boot-autoconfiguration-workflow)

[DataSourceAutoConfiguration 소스 코드 분석](#datasourceautoconfiguration-소스-코드-분석)

참고
- [1](https://www.marcobehler.com/guides/spring-boot-autoconfiguration)
- [2](https://docs.spring.io/spring-boot/reference/features/developing-auto-configuration.html)
- [3](https://openai.com/index/chatgpt/)
- 주석

## autoconfiguration 개요

자동 구성은 스프링 부트의 강력한 장점 중 하나로, 스프링 애플리케이션을 구동하기 위해 개발자가 해야 할 스프링이나 외부 라이브러리 등의 설정을 스프링 부트 차원에서 자동적으로 구성해주는 기능임

```java
@SpringBootApplication
public class BootApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}
```

IDE나 [spring initializr](https://start.spring.io)를 통해 스프링 부트 애플리케이션을 만들면 위와 같은 클래스와 main 메서드가 자동으로 생성되는데, 

`@SpringBootApplication` 어노테이션은 `@EnableAutoConfiguration`을 메타 어노테이션을 가지고 있음

`@EnableAutoConfiguration` 어노테이션이 스프링 부트의 자동 구성을 활성화하는 역할을 함   

스프링 부트 애플리케이션을 실행하면 스프링 부트는 `@AucoConfiguration` 어노테이션이 붙은 클래스들을 통해 자동 구성 기능을 실행하는데, 사용자가 유연한 구성(Configuration)을 할 수 있도록 3가지 기능을 제공함

### 1. @PropertySource 자동 등록

순수 스프링 프레임워크에서 외부의 프로퍼티 파일에 접근하려면 @PropertySource 어노테이션을 통해 프로퍼티 파일의 위치를 스프링에게 알려주면 됨

스프링 부트 애플리케이션을 구동하면 스프링 부트는 자동적으로 17가지의 다양한 경로로 프로퍼티 소스를 등록함

개발자가 여러 경로를 통해 프로퍼티 값을 설정하면 그에 따라 자동 구성이 이뤄짐

### 2. @AutoConfiguration 읽기

모든 스프링 부트 기반 프로젝트는 `org.springframework.boot:spring-boot-autoconfigure` 의존성을 가지고 있음

이 의존성은 단순한 jar 파일로 스프링 부트 자동 구성에 대한 모든 것을 담고 있는데

실질적으로 자동 구성을 담당하는 빈들은 `@AutoConfiguration` 적용된 클래스로, `org.springframework.boot:spring-boot-autoconfigure` jar 파일의 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 파일에 명시되어 있음

**스프링 부트가 구동될 때 마다 이 파일을 읽고, 각 빈들의 조건들(@Conditional)을 평가하여 자동 구성을 이룸**

### 3. 스프링 부트의 @Conditional

[스프링 부트 condition](./condition.md)

## spring.factories

스프링 프레임워크 내부에서 동적으로 특정 타입을 로딩할 때 이를 팩토리로 정의하고, META-INF/spring.factories 파일을 통해 필요한 클래스를 관리함
- 팩토리: 특정 타입의 클래스를 생성하거나 제공할 수 있는 클래스를 의미함 (AutoConfiguration 클래스, 이벤트 리스너, 컨버터 등)
- META-INF/spring.factories 파일은 동적 로딩이 필요한 스프링 jar 파일에 포함될 수 있음

클래스패스의 여러 jar 파일에 있는 META-INF/spring.factories에 지정된 타입에 맞는 클래스를 로딩하는 역할은 `SpringFactoriesLoader` 클래스가 수행함

```text
# spring-boot-autoconfigure 3.3.5 jar META-INF/spring.factories 파일 일부분

# ApplicationContext Initializers
org.springframework.context.ApplicationContextInitializer=\
org.springframework.boot.autoconfigure.SharedMetadataReaderFactoryContextInitializer,\
org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener
```

인터페이스나 추상 클래스 타입을 명시하고 인스턴스화할 특정 구현체를 컴마로 구분하여 나열함

위의 경우 ApplicationContextInitializer 인터페이스에 대한 구현체로 SharedMetadataReaderFactoryContextInitializer와 ConditionEvaluationReportLoggingListener를 지정함 

spring.factories를 통해 스프링은 직접적인 코드 참조없이도 동적으로 클래스를 인스턴스화할 수 있음

스프링 팀에서는 유연한 자동 구성 및 로직 중앙화를 위해 @AutoConfiguration을 스프링 3.0부터 새롭게 도입함 

META-INF/spring.factories, META-INF/spring-autoconfigure-metadata.properties, META-INF/spring/org.springframework.autoconfigure.AutoConfiguration.import 파일을 사용함

[참고하면 좋을 내용](https://github.com/spring-projects/spring-boot/issues/29698)

## @AutoConfiguration

스프링 부트 3.0 이전에는 여러 스프링 jar 파일의 META-INF/spring.factories 파일에 자동 구성 클래스를 명시했음

스프링 부트 3.0부터 여러 스프링 모듈에 나뉘어 있던 자동 구성 클래스에 대한 정보를 중앙화 하기 위해 spring autoconfigure 모듈의 META-INF/spring/org.springframework.autoconfigure.AutoConfiguration.import 파일에 모든 자동 구성 클래스 정보를 명시함

@AutoConfiguration 어노테이션은 클래스가 스프링 부트에 의해 자동 구성으로 동작하도록 표시하는 데 사용됨

proxyBeanMethods 속성 값을 false로 지정된 @AutoConfiguration을 메타 어노테이션으로 가지고 있어서  bean 메서드에 대한 프록시 처리를 하지 않음

이 어노테이션이 붙어있으면 spring.factories에 명시하지 않아도 되며 @Conditional 조건문과 결합하여 특정 조건이 충족할 때 스프링 부트가 자동으로 빈 등록 및 초기화 작업을 수행할 수 있게 함

또한 @AutoConfigureBefore과 @AutoConfigureAfter 어노테이션을 통해 구성 클래스의 적용 순서를 지정할 수도 있음

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore
@AutoConfigureAfter
public @interface AutoConfiguration {
    
	@AliasFor(annotation = Configuration.class)
	String value() default "";

    @AliasFor(annotation = AutoConfigureBefore.class, attribute = "value")
    Class<?>[] before() default {};

	@AliasFor(annotation = AutoConfigureBefore.class, attribute = "name")
	String[] beforeName() default {};
    
	@AliasFor(annotation = AutoConfigureAfter.class, attribute = "value")
	Class<?>[] after() default {};
    
	@AliasFor(annotation = AutoConfigureAfter.class, attribute = "name")
	String[] afterName() default {};

}
```

## @EnableAutoConfiguration

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
    // ...
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage {
    // ...
}
```

`@EnableAutoConfiguration` 어노테이션은 @SpringBootApplication 어노테이션에 선언되어 있는 어노테이션으로 스프링 부트 자동 구성을 활성화 하기 위해 사용됨

스프링 부트 애플리케이션이 실행되면 클래스패스 상의 라이브러리를 감지하여 적절한 스프링 빈 설정을 자동으로 적용함

메타 어노테이션으로 선언된 `AutoConfigurationPackage` 어노테이션을 통해 애플리케이션의 기본 패키지(컴포넌트 스캔 대상)를 자동 구성의 스캔 대상에 포함시킴

또한 AutoConfigurationImportSelector 클래스를 import해서 META-INF/spring/AutoConfiguration.imports 파일에 나열된 @AutoConfiguration 클래스들을 로드함

자동 구성 클래스는 `@ConditionalOnClass`, `@ConditionalOnProperty` 같은 어노테이션을 통해 클래스패스나 애플리케이션 컨텍스트, 프로퍼티 등에 따라 활성화/비활성화됨

## Spring Boot AutoConfiguration Workflow

```text
# spring-boot-autoconfigure 3.3.5 jar META-INF/spring/org.springframework.autoconfigure.AutoConfiguration.imports 파일 일부분

org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration
org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration
org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration
```

1. 스프링 부트 애플리케이션 시작 `SpringApplication.run()`
2. `@SpringBootApplication`의 `@EnableAutoConfiguration` 어노테이션이 스프링 부트의 자동 구성 로직을 활성화함 (AutoConfigurationImportSelector import 및 @AutoConfigurationPackage)
3. AutoConfigurationImportSelector 클래스는 AutoConfiguration.imports 파일을 읽어서 자동 구성 클래스를 스캔하고, 자동 구성 클래스마다 적용된 @Conditional 평가 결과에 따라 선택하여 애플리케이션 컨텍스트에 등록
4. 등록된 자동 구성 클래스들의 @Bean 메서드 수행 (마찬가지로 @Conditional 평가에 따라 애플리케이션 등록 여부 결정)
5. 필요한 모든 자동 구성 완료

## DataSourceAutoConfiguration 소스 코드 분석

DataSourceAutoConfiguration 클래스는 JDBC의 DataSource 빈과 관련된 설정들을 자동으로 구성해줌

```java
package org.springframework.boot.autoconfigure.jdbc;

// 자동 구성 클래스임을 나타냄, before 속성을 통해 SqlInitializationAutoConfiguration 클래스가 먼저 자동 구성이 이뤄져야 함을 나타냄
@AutoConfiguration(before = SqlInitializationAutoConfiguration.class)
// 클래스패스에 DataSource 타입과 EmbeddedDataSourceType 타입이 존재하는 경우
// JDBC 관련 의존성 추가 시 DataSource와 EmbeddedDatabaseType(spring jdbc 모듈) 타입이 클래스패스에 추가됨
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
// 클래스패스에 지정한 타입의 빈이 없는 경우
// ConnectionFactory가 있는 경우 R2DBC 환경으로 판단하므로 JDBC 자동 구성 설정을 하지 않음
@ConditionalOnMissingBean(type = "io.r2dbc.spi.ConnectionFactory")
// 데이터베이스 설정 값을 가진 DataSourceProperties 클래스 스프링 빈 등록
@EnableConfigurationProperties(DataSourceProperties.class)
// DataSourcePoolMetadataProvidersConfiguration: 데이터 소스 풀 메타데이터 관리 구성 클래스
// DataSourceCheckpointRestoreConfiguration: 체크포인트 관리 구성 클래스(스냅샷 관리 등)
@Import({ DataSourcePoolMetadataProvidersConfiguration.class, DataSourceCheckpointRestoreConfiguration.class })
public class DataSourceAutoConfiguration {

    /*
        JDBC 관련 의존성을 추가했으나 개발자 데이터베이스 관련 설정을 별도로 하지 않은 경우
        스프링 부트는 임베디드 데이터베이스 구성 클래스를 스프링 빈으로 등록하여 인메모리 데이터베이스를 임시로 사용할 수 있게 함
        
        다음의 조건들을 만족해야 인메모리 데이터베이스 구성 클래스가 로드됨
        1. 내부 클래스인 EmbeddedDatabaseCondition 조건 부합
        2. DataSource와 XADataSource 타입의 스프링 빈이 없는 경우 (개발자가 별도로 스프링 빈 등록을 하지 않은 경우)
        
        조건에 부합한다면 EmbeddedDataSourceConfiguration 클래스를 애플리케이션 컨텍스트에 로드함
     */
	@Configuration(proxyBeanMethods = false)
	@Conditional(EmbeddedDatabaseCondition.class)
	@ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
	@Import(EmbeddedDataSourceConfiguration.class)
	protected static class EmbeddedDatabaseConfiguration {

	}

	@Configuration(proxyBeanMethods = false)
	@Conditional(PooledDataSourceCondition.class)
	@ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
	@Import({ DataSourceConfiguration.Hikari.class, DataSourceConfiguration.Tomcat.class,
			DataSourceConfiguration.Dbcp2.class, DataSourceConfiguration.OracleUcp.class,
			DataSourceConfiguration.Generic.class, DataSourceJmxConfiguration.class })
	protected static class PooledDataSourceConfiguration {

		@Bean
		@ConditionalOnMissingBean(JdbcConnectionDetails.class)
		PropertiesJdbcConnectionDetails jdbcConnectionDetails(DataSourceProperties properties) {
			return new PropertiesJdbcConnectionDetails(properties);
		}

	}
    
    
	static class PooledDataSourceCondition extends AnyNestedCondition {

		PooledDataSourceCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnProperty(prefix = "spring.datasource", name = "type")
		static class ExplicitType {

		}

		@Conditional(PooledDataSourceAvailableCondition.class)
		static class PooledDataSourceAvailable {

		}

	}
    
	static class PooledDataSourceAvailableCondition extends SpringBootCondition {

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			ConditionMessage.Builder message = ConditionMessage.forCondition("PooledDataSource");
			if (DataSourceBuilder.findType(context.getClassLoader()) != null) {
				return ConditionOutcome.match(message.foundExactly("supported DataSource"));
			}
			return ConditionOutcome.noMatch(message.didNotFind("supported DataSource").atAll());
		}

	}
    
    // 임베디드 데이터베이스가 필요한지 파악하는 Condition
	static class EmbeddedDatabaseCondition extends SpringBootCondition {

		private static final String DATASOURCE_URL_PROPERTY = "spring.datasource.url";

		private final SpringBootCondition pooledCondition = new PooledDataSourceCondition();

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			ConditionMessage.Builder message = ConditionMessage.forCondition("EmbeddedDataSource");
            
            // 데이터베이스 URL 프로퍼티를 설정한 경우 임베디드 데이터베이스가 필요없다고 판단 
			if (hasDataSourceUrlProperty(context)) {
				return ConditionOutcome.noMatch(message.because(DATASOURCE_URL_PROPERTY + " is set"));
			}
            // 
			if (anyMatches(context, metadata, this.pooledCondition)) {
				return ConditionOutcome.noMatch(message.foundExactly("supported pooled data source"));
			}
			EmbeddedDatabaseType type = EmbeddedDatabaseConnection.get(context.getClassLoader()).getType();
			if (type == null) {
				return ConditionOutcome.noMatch(message.didNotFind("embedded database").atAll());
			}
			return ConditionOutcome.match(message.found("embedded database").items(type));
		}

        // 개발자가 데이터베이스 URL 프로퍼티를 설정한 경우 true 반환
		private boolean hasDataSourceUrlProperty(ConditionContext context) {
			Environment environment = context.getEnvironment();
			if (environment.containsProperty(DATASOURCE_URL_PROPERTY)) {
				try {
					return StringUtils.hasText(environment.getProperty(DATASOURCE_URL_PROPERTY));
				}
				catch (IllegalArgumentException ex) {
					// Ignore unresolvable placeholder errors
				}
			}
			return false;
		}

	}

}
```