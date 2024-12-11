[JPA Transaction](#jpa-transaction)
- [EntityManager EntityTransaction](#entitymanager-entitytransaction)
- [JPA Transaction Propagation](#jpa-transaction-propagation)

[Spring Data JPA Transaction](#spring-data-jpa-transaction)
- [PlatformTransactionManager](#platformtransactionmanager)
- [JpaTransactionManager](#jpatransactionmanager)
- [Spring Transaction Propagation](#spring-transaction-propagation)
- [Transaction Isolation Level](#transaction-isolation-level)
- [@Transactional](#transactional)
- [Query Method Transaction](#query-method-transaction)
- [Rollback Rules](#rollback-rules)
- [DataSource, Connection Pooling, Spring Transaction Context](#datasource-connection-pooling-spring-transaction-context)

## JPA Transaction

트랜잭션은 데이터베이스에서 하나의 작업 단위를 나타내는데, 여러 작업을 묶어 하나의 작업처럼 처리함

#### ACID 속성
- Atomicity: 모든 작업 성공 or 실패(원자성)
- Consistency: 트랜잭션 전후 데이터 일관성 유지
- Isolation: 트랜잭션 간 독립성 보장
- Durability: 트랜잭션 커밋 후 영구히 저장

JPA에서 제공하는 트랜잭션 관련 객체: EntityManager, EntityTransaction

## EntityManager, EntityTransaction

JPA 트랜잭션 관리는 EntityManager를 통해 트랜잭션을 획득하고, 명시적으로 트랜잭션 관련 메서드를 선언하여 수행됨

엔티티 매니저는 트랜잭션이 끝나면 더 이상 사용할 수 없음

트랜잭션 범위 내에서만 엔티티를 조작할 수 있으며 트랜잭션 커밋 시 영속성 컨텍스트에 관리되고 있는 엔티티들이 데이터베이스에 반영됨

```java
/*
    EntityTransaction 객체 반환 메서드
    begin(), commit(), rollback() 메서드를 통해 명시적으로 트랜잭션을 관리함
*/
EntityTransaction transaction = entityManager.getTransaction();

try {
    // 트랜잭션 시작
    transaction.begin();
    
    // 영속성 컨텍스트에 엔티티 영속화
    entityManager.persist(simpleEntity);
    
    //트랜잭션 커밋
    transaction.commit();
} catch (Exception e) {
    
    // 예외 발생 시 트랜잭션 롤백
    transaction.rollback();
} finally {
    entityManager.close();
}
```

## JPA Transaction Isolation Level

JPA는 기본적으로 데이터베이스의 격리 수준을 따름

별도의 트랜잭션 격리 수준 설정을 지원하지 않으며, 데이터베이스 또는 스프링으로 설정할 수 있음

## JPA Transaction Propagation

JPA 자체에서 트랜잭션 전파 기능을 지원하지 않음

스프링의 트랜잭션 관리로 보완할 수 있음

## Spring Data JPA Transaction

## PlatformTransactionManager

## JpaTransactionManager

## TransactionSynchronizationManager

## Spring Transaction Propagation

## Transaction Isolation Level

## @Transactional

## Query Method Transaction

## Rollback Rules

## DataSource, Connection Pooling, Spring Transaction Context