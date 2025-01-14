[spring data redis](#spring-data-redis)

[구성 요소](#구성-요소)

[설정](#설정)

[RedisTemplate](#redistemplate-1)

[redis cache store](#redis-cache-store)

[pub/sub](#pubsub)

## spring data redis

redis와 spring 애플리케이션 간의 통합을 쉽게 해주는 스프링 데이터 프로젝트

redis 저장소 상호작용

reids 데이터 타입 지원

redis 캐싱 기능

redis pub/sub 메시징 기능 등


## 구성 요소

#### RedisTemplate

레디스와 상호작용하기 위한 스프링에서 제공하는 템플릿 클래스

JdbcTemplate처럼 레디스와 관련해서 공통적으로 수행해야 할 로직을 구현하고 동적으로 변경될 부분들을 템플릿 메서드로 처리한다

레디스의 모든 데이터 타입을 지원한다

레디스 명령을 코드에서 수행할 수 있다

#### ReactiveRedisTemplate

비동기 방식으로 레디스와 통합하기 위한 템플릿

WebFlux와 같은 리액티브 프로그래밍 환경에서 사용한다

#### CacheManager

Spring Cache를 추상화하여 레디스를 캐시 스토어로 활용할 수 있게 해주는 인터페이스

레디스 기반의 캐싱 구현을 단순화한다

#### RedisConnectionFactory

#### RedisMessageListenerContainer

레디스 pub/sub 메시지를 수신하기 위한 구성 요소

메시징 애플리케이션 구현 시 사용한다


## 설정

#### 의존성 설정

스프링 부트 스타터를 사용하면 스프링 데이터와 레디스 클라이언트 의존성을 간단하게 명시할 수 있다

스타터는 레디스의 jedis와 lettuce 클라이언트를 가져온다

```kotlin
implementation("org.springframework.boot:spring-boot-starter-data-redis")
```

#### 레디스 설정

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      username: test
      password: test
```

```java
@Configuration
public class RedisConfig {

    // 스프링 부트가 LettuceConnectionFactory 타입으로 자동 구성한다
    // 별도의 설정이 필요한 경우 빈으로 등록
    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new Le("localhost", 6379);
    }

    // 스프링 부트가 RedisTemplate<Object, Object>와 StringRedisTemplate을 자동 구성한다
    // 별도의 설정이 필요한 경우 빈으로 등록
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // 레디스 클라이언트 설정
        template.setConnectionFactory(lettuceConnectionFactory());

        // 키, 값 직렬화 설정
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

}
```

```java
@Configuration
// 캐시 스토어 사용 선언
@EnableCaching
public CacheConfig {

    // 캐시 매니저 등록
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        // 캐시 설정
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // 키, 값 직렬화 설정
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues() // null 값 캐시 방지
                .entryTtl(Duration.ofMinutes(10)); // 캐시 만료 시간 설정

        return RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory) // 클라이언트 설정
                .cacheDefaults(redisCacheConfiguration) // 캐시 설정
                .build();
    }
}
```


## RedisTemplate

### `opsFor<data type>()`

RedisTemplate의 메서드 이름은 레디스의 데이터 구조와 작업 방식에 따라 명확하게 지어져있다

레디스에서 지원하는 데이터 타입에 따라 작업을 수행할 수 있는 객체를 반환한다

| 데이터 타입           | 메서드                 | 반환 타입                 |
|------------------|---------------------|-----------------------|
| string           | opsForValue()       | ValueOperations       |
| hash             | opsForHash()        | HashOperations        |
| list             | opsForList()        | ListOperations        |
| set              | opsForSet()         | SetOperations         |
| sorted set(zset) | opsForZSet()        | ZSetOperations        |
| hyperloglog      | opsForHyperLogLog() | HyperLogLogOperations |
| cluster          | opsForCluster()     | ClusterOperations     |
| geo              | opsForGeo()         | GeoOperations         |
| stream           | opsForStream()      | StreamOperations      |

### `bound<data type>Ops()`

특정 키에 바인딩된 데이터 타입에 따라 작업을 수행할 수 있는 객체를 반환한다

특정 키에 대한 컨텍스트를 유지하여 동일한 키에 대해 여러 작업을 수행할 때, 키를 매번 명시하지 않아도 된다

| 데이터 타입            | 메서드                 | 반환 타입                 |
|-------------------|---------------------|-----------------------|
| string            | boundValueOps(key)  | BoundValueOperations  |
| hash              | boundHashOps(key)   | BoundHashOperations   |
| list              | boundListOps(key)   | BoundListOperations   |
| set               | boundSetOps(key)    | BoundSetOperations    |
| sorted set (zset) | boundZSetOps(key)   | BoundZSetOperations   |
| geo               | boundGeoOps(key)    | BoundGeoOperations    |
| stream            | boundStreamOps(key) | BoundStreamOperations |




## redis cache store




## pub/sub