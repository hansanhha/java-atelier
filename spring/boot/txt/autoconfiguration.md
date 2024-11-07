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

실질적으로 자동 구성을 담당하는 빈들은 `@AutoConfiguration` 적용된 클래스로, `org.springframework.boot:spring-boot-autoconfigure` jar 파일의 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 파일에 명시되어 있음

**스프링 부트가 구동될 때 마다 이 파일을 읽고, 각 빈들의 조건들(@Conditional)을 평가하여 자동 구성을 이룸**

### 3. 스프링 부트의 @Conditional

`@Conditional`은 스프링 프레임워크에서 제공하는 로우 레벨 어노테이션이고

스프링 부트는 개발자가 다양한 조건문을 작성할 수 있도록 추가적인 `@Conditional` 어노테이션을 제공함

#### Class Conditions

`@ConditionalOnClass`: 클래스 패스에 지정한 클래스가 있는 경우 true 반환

`@ConditionalOnMissingClass`: 클래스 패스에 지정한 클래스가 없는 경우 true 반환

#### Bean Conditions

`@ConditionalOnBean`: ApplicationContext에 지정한 타입의 빈이 이미 있는 경우 true 반환

`@ConditionalOnMissingBean`ApplicationContext에 지정한 타입의 빈이 아직 없는 경우 true 반환

#### Property Conditions

`@ConditionalOnProperty`: ApplicationContext에 특정 속성이 설정된 경우 true 반환

#### Resource Conditions

`@ConditionalOnResource`: ApplicationContext에 특정 리소스가 존재하는 경우 true 반환

#### Other Conditions

`@ConditionalOnSingleCandidate`: ApplicationContext에 특정 타입의 빈이 존재하거나, 여러 타입이 있더라도 해당 타입의 primary 빈이 설정된 경우 true 반환

### @AutoConfiguration



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

## 소스 코드 분석 (DataSourceAutoConfiguration)

DataSourceAutoConfiguration 클래스는 JDBC의 DataSource 빈과 관련된 설정들을 자동으로 구성해줌

```java
package org.springframework.boot.autoconfigure.jdbc;

@AutoConfiguration(before = SqlInitializationAutoConfiguration.class)
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
@ConditionalOnMissingBean(type = "io.r2dbc.spi.ConnectionFactory")
@EnableConfigurationProperties(DataSourceProperties.class)
@Import({ DataSourcePoolMetadataProvidersConfiguration.class, DataSourceCheckpointRestoreConfiguration.class })
public class DataSourceAutoConfiguration {

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
    
	static class EmbeddedDatabaseCondition extends SpringBootCondition {

		private static final String DATASOURCE_URL_PROPERTY = "spring.datasource.url";

		private final SpringBootCondition pooledCondition = new PooledDataSourceCondition();

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			ConditionMessage.Builder message = ConditionMessage.forCondition("EmbeddedDataSource");
			if (hasDataSourceUrlProperty(context)) {
				return ConditionOutcome.noMatch(message.because(DATASOURCE_URL_PROPERTY + " is set"));
			}
			if (anyMatches(context, metadata, this.pooledCondition)) {
				return ConditionOutcome.noMatch(message.foundExactly("supported pooled data source"));
			}
			EmbeddedDatabaseType type = EmbeddedDatabaseConnection.get(context.getClassLoader()).getType();
			if (type == null) {
				return ConditionOutcome.noMatch(message.didNotFind("embedded database").atAll());
			}
			return ConditionOutcome.match(message.found("embedded database").items(type));
		}

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