[Spring Data JPA](#spring-data-jpa)

[JpaRepository<T, ID>](#jparepositoryt-id)
- [JpaRepositoryImplementation<T, ID>](#jparepositoryimplementationt-id)
- [SimpleJpaRepository](#simplejparepository)

## Spring Data JPA

스프링 데이터의 하위 프로젝트로, JPA 데이터 접근을 추상화하여 스프링 데이터 접근 계층에서

Hibernate, EclipseLink 등 JPA 프로바이더를 손쉽게 사용할 수 있음

주요 기능

- Repository 인터페이스 제공
- 메서드 이름 기반의 쿼리 생성
- 페이징 및 정렬 지원
- JPQL 및 네이티브 쿼리 지원
- 동적 쿼리 지원
- Auditing 기능 제공

## JpaRepository<T, ID>

Spring Data JPA에서 제공하는 주요 인터페이스로,

`Repository<T, ID>`, `ListCrudRepository<T, ID>`, `ListPagingAndSortingRepository<T, ID>`, `QueryByExampleExecutor<T>` 를
확장하고, 추가적인 메서드를 제공함

```java

@NoRepositoryBean
public interface JpaRepository<T, ID> extends ListCrudRepository<T, ID>, ListPagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
    /*
        EntityManager의 변경 사항을 데이터베이스에 강제로 동기화(flusH)함
        현재까지의 변경 사항을 즉시 데이터베이스에 반영하고 싶을 때 사용
     */
    void flush();

    // 주어진 엔티티를 저장한 후, 즉시 데이터베이스에 동기화함
    <S extends T> S saveAndFlush(S entity);

    <S extends T> List<S> saveAllAndFlush(Iterable<S> entities);

    /** @deprecated */
    @Deprecated
    default void deleteInBatch(Iterable<T> entities) {
        this.deleteAllInBatch(entities);
    }

    /*
        주어진 엔티티들을 배치로 삭제함
        삭제된 엔티티를 1차 캐시(영속성 컨텍스트)에서 제거하지 않으며, 성능을 위해 데이터베이스에 직접 삭제 명령을 보냄
        대량의 엔티티를 한 번에 삭제하고자 할 때 사용
     */
    void deleteAllInBatch(Iterable<T> entities);

    // ID 컬렉션을 기반으로 배치 삭제
    void deleteAllByIdInBatch(Iterable<ID> ids);

    // 데이터베이스의 모든 엔티티들을 배치로 삭제
    void deleteAllInBatch();

    /** @deprecated */
    @Deprecated
    T getOne(ID id);

    /** @deprecated */
    @Deprecated
    T getById(ID id);

    /*
        주어진 ID에 대한 엔티티의 지연 로딩(Lazy Loading) 참조를 반환함
        데이터베이스 조회를 지연시키고, 엔티티의 참조를 즉시 반환해야 할 때 사용됨
        getOne(), getById() 메서드를 대체함
     */
    T getReferenceById(ID id);

    /*
        Query By Example(QBE) 기능을 사용하여 특정 조건에 맞는 엔티티를 검색하는 메서드
        Example은 엔티티의 예제 인스턴스로, 일치 조건 여부 동적 쿼리를 생성할 때 사용됨 
     */
    <S extends T> List<S> findAll(Example<S> example);

    // 정렬 조건 추가, 지정된 정렬 순서에 따라 결과를 정렬함
    <S extends T> List<S> findAll(Example<S> example, Sort sort);
}
```

### JpaRepositoryImplementation<T, ID>

스프링 데이터 JPA 내부에서 사용되는 인터페이스로, 스프링 데이터 JPA가 런타임에 Repository 인터페이스를 실제 구현체로 생성할 때 필요한 내부 기능을 정의함

JpaRepository 및 동적 쿼리를 생성 및 실행하는 JpaSpecificationExecutor와 스프링 데이터 JPA 내부에서 리포지토리를 설정하는 데 사용되는 JpaRepositoryConfigurationAware를 확장함

```java
@NoRepositoryBean
public interface JpaRepositoryImplementation<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>, JpaRepositoryConfigurationAware {
}
```

### SimpleJpaRepository

스프링 데이터 JPA에서 제공하는 Repository의 기본 구현체임

개발자가 리포지토리 인터페이스를 정의할 때마다, 자동으로 인터페이스의 구현체를 생성해 주는데 이 때 기본적으로 사용되는 구현체가 SimpleJpaRepository임

#### 상속 관계

<img src="../images/SimpleJpaRepository-hierarchy.png" alt="simple jpa repository hierarchy" style="width:50%; height:50;">

JpaRepositoryImplementation 인터페이스 및 그 상위 인터페이스를 모두 구현하고 있음

JPA를 사용하여 데이터베이스와 상호작용하는 데 필요한 대부분의 표준 메서드들을 구현함 

- CRUD 작업
- 페이징
- 정렬
- flush, deleteInBatch() 등

```java
@Repository
@Transactional(
        readOnly = true
)
public class SimpleJpaRepository<T, ID> implements JpaRepositoryImplementation<T, ID> {
    ...
}
```

`@Repository`와 `@Transacitonal`(readOnly) 어노테이션을 적용함 

#### SimpleJpaRepository 동작 과정

리포지토리 인터페이스 정의
- ```java
  public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByLastName(String lastName);
  }
  ```

애플리케이션 컨텍스트 초기화
- 스프링 애플리케이션이 시작될 때, 애플리케이션 컨텍스트가 초기화되면서 스프링 데이터 JPA가 리포지토리 인터페이스들을 스캔함
- 스캔 과정에서 `@EnableJpaRepositories` 어노테이션이 설정된 패키지 내의 모든 리포지토리 인터페이스를 찾아서, 빈으로 등록함

리포지토리 인터페이스의 구현체 생성 및 사용
- 스프링 데이터 JPA는 [JpaRepositoryFactory](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/support/JpaRepositoryFactory.html)를 사용하여 각 리포지토리 인터페이스의 구현체를 생성함
- 구현체를 생성할 때 리포지토리 인터페이스에 대한 프록시 객체를 생성함
- 해당 프록시 객체의 실제 구현체가 SimpleJPaRepository임
- 사용자가 `UserRepository`와 같은 리포지토리를 주입받을 때 프록시 객체를 주입하고, 프록시 객체가 실행할 메서드를 가로채서 SimpleJpaRepository의 메서드를 호출함

## PlatformTransactionManager

## TransactionSynchronizationManager

## @Transaction

## @Transactional

## Query Method

## Projections

## Specifications

## Locking

## Auditing

## Custom Repository

## Publishing Domain Events

## Repository Query Keyword

## Repository Return type