[JPA Transaction](#jpa-transaction)
- [EntityManager EntityTransaction](#entitymanager-entitytransaction)
- [JPA Transaction Propagation](#jpa-transaction-propagation)

[Spring Data JPA Transaction](#spring-data-jpa-transaction)
- Transaction Objects
  - [TransactionDefinition](#transactiondefinition)
  - [TransactionStatus, TransactionExecution, SavepointManager](#transactionstatus-transactionexecution-savepointmanager)
- Transaction Managers
  - [PlatformTransactionManager](#platformtransactionmanager)
  - [AbstractPlatformTransactionManager](#abstractplatformtransactionmanager)
  - [JpaTransactionManager](#jpatransactionmanager)
  - [TransactionSynchronizationManager](#transactionsynchronizationmanager)
- Spring's Transaction Behavior
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
    EntityTransaction의 begin(), commit(), rollback() 메서드를 통해 명시적으로 트랜잭션을 관리함
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

스프링 데이터 JPA는 스프링의 트랜잭션 관리 및 JPA와 통합하여 선언적으로 트랜잭션을 관리하거나 영속성 컨텍스트 변경사항을 자동으로 반영하는 메커니즘을 제공함

주요 컴포넌트: `@Transactional` `JpaTransactionManager` `TransactionSynchronizationManager`

## TransactionDefinition

트랜잭션 설정 정보 보관 객체
- 트랜잭션 이름(`name`)
- 트랜잭션 전파(`PROPAGATION_*`): 새로운 트랜잭션 생성/기존 트랜잭션 참여 여부 설정 및 트랜잭션이 없는 상태에서의 처리 방식 정의
- 격리 수준(`ISOLATION_*`): 데이터베이스 격리성 수준 설정
- 최대 지속 시간(`TIMEOUT_DEFAULT`): 트랜잭션 최대 지속 시간 설정(시간 내에 트랜잭션이 완료되지 않으면 롤백 처리)
- 읽기 전용(`readOnly``): 트랜잭션 읽기 전용 여부 설정

트랜잭션 전파, 격리 수준은 인터페이스 상수값(public static final)으로 정의함

모든 설정 값 조회 메서드를 default 메서드로 정의함

**트랜잭션 전파 설정 값으로 TransactionManager와 TransactionSynchronizationManager의 동작을 결정함**

```java
public interface TransactionDefinition {

    /*------------------------------
           트랜잭션 전파 설정 값
    ------------------------------*/
    
    /*
        트랜잭션 정의/트랜잭션 동기화 범위의 기본 값
        
        트랜잭션 매니저
        - 기존 트랜잭션이 있으면 해당 트랜잭션 컨텍스트 참여
        - 없으면 새 트랜잭션 생성
        
        트랜잭션 동기화
        - 기존 트랜잭션 참여 시 동일한 동기화 리소스 공유
        - 새 트랜잭션 참여 시 새로운 동기화 리소스 초기화 
    */
    int PROPAGATION_REQUIRED = 0;

    /*
        트랜잭션 매니저
        - 기존 트랜잭션이 있으면 해당 트랜잭션 컨텍스트 참여
        - 없으면 트랜잭션 없이 실행
        
        트랜잭션 동기화
        - 기존 트랜잭션이 있는 경우만 동일한 동기화 리소스 공유
        - 없으면 동기화 리소스 작업 생략
     */
    int PROPAGATION_SUPPORTS = 1;

    /*
        트랜잭션 매니저
        - 기존 트랜잭션이 있으면 해당 트랜잭션 컨텍스트 참여
        - 기존 트랜잭션이 없으면 IllegalTransactionStateException 발생
        
        트랜잭션 동기화
        - 기존 트랜잭션이 있는 경우만 동일한 동기화 리소스 공유
        - 없으면 실행되지 않음
     */
    int PROPAGATION_MANDATORY = 2;

    /*
        독립적인 작업(로깅, 외부 시스템 호출)에 적합한 옵션
        성능 및 복잡성 고려 필요
    
        트랜잭션 매니저
        - 기존 트랜잭션이 있으면 일시적으로 보류하고, 새 트랜잭션 생성
        - 새 트랜잭션이 종료되면 보류한 기존 트랜잭션 활성화
        
        트랜잭션 동기화
        - 새로운 트랜잭션에 대한 별도의 동기화 리소스 생성
        - 기존 트랜잭션의 동기화 리소스는 임시로 저장
     */
    int PROPAGATION_REQUIRES_NEW = 3;

    /*
        트랜잭션 매니저
        - 기존 트랜잭션이 있으면 일시적으로 보류하고, 트랜잭션 없이 작업 수행
        - 기존 트랜잭션이 없으면 트랜잭션 없이 바로 작업 수행
        
        트랜잭션 동기화
        - 트랜잭션 동기화 리소스 사용 X
        - 기존 트랜잭션 동기화 리소스는 이후 다시 활성화됨
     */
    int PROPAGATION_NOT_SUPPORTED = 4;
    
    /*
        트랜잭션 매니저
        - 기존 트랜잭션이 있으면 IllegalTransactionStateException 발생
        - 기존 트랜잭션이 없으면 트랜잭션 없이 바로 작업 수행
        
        트랜잭션 동기화
        - 트랜잭션 동기화 리소스 사용 X
     */
    int PROPAGATION_NEVER = 5;

    /*
        트랜잭션 매니저
        - 기존 트랜잭션이 있으면 SavePoint를 생성하여 중첩 트랜잭션 지원
        - 기존 트랜잭션이 없으면 새 트랜잭션 생성
        
        트랜잭션 동기화
        - 중첩 트랜잭션의 경우 동일한 동기화 리소스 사용, SavePoint를 통해 상태 관리
        - 독립적인 트랜잭션처럼 동작하지만 부모 트랜잭션에 영향받음
     */
    int PROPAGATION_NESTED = 6;
    
    /*------------------------------
           격리 수준 설정 값
    ------------------------------*/

    int ISOLATION_DEFAULT = -1;

    int ISOLATION_READ_UNCOMMITTED = 1;

    int ISOLATION_READ_COMMITTED = 2;

    int ISOLATION_REPEATABLE_READ = 4;

    int ISOLATION_SERIALIZABLE = 8;
    
    /*------------------------------
           시간 초과 설정 값
    ------------------------------*/

    int TIMEOUT_DEFAULT = -1;

    /*------------------------------
         설정 값 조회 메서드 정의
    ------------------------------*/

    // 트랜잭션 전파 기본 행동
    default int getPropagationBehavior() {
        return PROPAGATION_REQUIRED;
    }

    // 기본 격리 수준
    default int getIsolationLevel() {
        return ISOLATION_DEFAULT;
    }

    // 기본 시간 초과
    default int getTimeout() {
        return TIMEOUT_DEFAULT;
    }
    
    // 기본 read-only 여부
    default boolean isReadOnly() {
        return false;
    }

    // 기본 트랜잭션 이름
    @Nullable
    default String getName() {
        return null;
    }

    // 기본 설정 값 인스턴스 사용, default 메서드 기본 로직 그대로 사용
    static TransactionDefinition withDefaults() {
        return StaticTransactionDefinition.INSTANCE;
    }
}
```

## TransactionStatus, TransactionExecution, SavepointManager

트랜잭션의 상태를 관리하는 인터페이스로 PlatformTransactionManager와 함께 동작함

사용 시점
- 트랜잭션 시작
  - `PlatformTransactionManager.getTransaction(TransactionDefinition)` 호출
  - 반환되는 `TransactionStatus` 객체를 통해 트랜잭션 상태 추적
- 트랜잭션 처리
  - 비즈니스 로직 실행 중 `TransactionStatus`를 참조하여 롤백 여부, 새로운 트랜잭션 여부 확인
- 트랜잭션 커밋/롤백
  - `commit(TrasactionStatus)` `rollback(TransactionStatus)`

```java
public interface TransactionStatus extends TransactionExecution, SavepointManager, Flushable {

    default boolean hasSavepoint() {
      return false;
    }
  
    @Override
    default void flush() {
    }
}
```

TransactionStatus는 TransactionExecution, SavepointManager, Flushable 인터페이스를 확장함

### TransactionExecution

[예시 코드](../src/main/java/spring/data/jpa/transaction/TransactionStatusExample.java)

트랜잭션의 현재 상태 정보를 제공하는 인터페이스

TransactionStatus, ReactiveTransaction, TransactionExecutionListener에서 사용

정의된 메서드(모두 default 메서드로 정의)
- 트랜잭션 이름
- 트랜잭션 활성화 여부
- 새 트랜잭션 여부
- 중첩 트랜잭션 여부
- 읽기 전용 여부
- 롤백 필수 여부
- 완료 여부
- 롤백 설정

```java
public interface TransactionExecution {
    
    default String getTransactionName() {
        return "";
    }
    
    /*    
        활성화된 트랜잭션인지 확인
        새 트랜잭션 또는 기존 트랜잭션에 참여한 경우 true 반환
     */
    default boolean hasTransaction() {
        return true;
    }

    /*
        새 트랜잭션 여부 반환
        중첩 트랜잭션의 경우 트랜잭션 매니저에 따라 새 트랜잭션으로 판단할 수 있기 때문에
        isNested 메서드와 함께 복합 체크
     */
    default boolean isNewTransaction() {
        return true;
    }
    
    default boolean isNested() {
        return false;
    }

    default boolean isReadOnly() {
        return false;
    }

    default void setRollbackOnly() {
        throw new UnsupportedOperationException("setRollbackOnly not supported");
    }

    default boolean isRollbackOnly() {
        return false;
    }

    default boolean isCompleted() {
        return false;
    }
    
}
```

### SavepointManager

Savepoint: 트랜잭션 내에서 특정 시점으로 롤백할 수 있도록 설정하는 기능

SavepointManager는 트랜잭션 저장 지점을 다루는 인터페이스임
- 중간 롤백: 특정 지점까지 작업 수행 후, 오류가 발생하면 저장 지점으로 롤백(일부 작업만 취소)
- 단위 작업 관리

기존 트랜잭션이 있는 상황에서 트랜잭션 전파를 `TransactionDefinition.PROPAGATION_NESTED`로 설정한 경우 

Savepoint 기능을 사용하는 트랜잭션 매니저 구현체라면 AbstractTransactionStatus가 확장한 SavepointManager의 API를 사용함

```java
public interface SavepointManager {
    
    // 현재 트랜잭션의 savepoint를 생성하고, savepoint를 나타내는 객체 반환
    Object createSavepoint() throws TransactionException;

    // 특정 savepoint로 롤백(해당 savepoint 이후 모든 작업 취소)
    void rollbackToSavepoint(Object savepoint) throws TransactionException;

    // savepoint 해제
    void releaseSavepoint(Object savepoint) throws TransactionException;
}
```

## PlatformTransactionManager

스프링 프레임워크 자체에서 제공하는 스프링의 트랜잭션 관리 중앙 인터페이스

생각보다 몇 개 되지 않는 메서드만 정의되어 있지만 사악한 주석으로 메서드 행동을 설명하고 있음

```java
public interface PlatformTransactionManager extends TransactionManager {

    /*
        파라미터로 주어진 TransactionDefinition의 propagation 설정값에 따라서
        현재 활성화된 트랜잭션 또는 새로 생성한 트랜잭션을 반환함
        
        또한 isolation level이나 timeout 설정 값은 
        새로운 트랜잭션에만 적용되고 이미 참여하고 있는 트랜잭션에는 무시됨
     */
    TransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException;

    /*
        getTransaction 메서드에서 반환한 TransactionStatus의 설정값에 따라 커밋을 수행함
        rollback-only 옵션이 설정되어 있는 경우 롤백을 수행함
     */
    void commit(TransactionStatus status) throws TransactionException;

    // 주어진 transaction 롤백
    void rollback(TransactionStatus status) throws TransactionException;
}
```

## AbstractPlatformTransactionManager

스프링은 최상위 인터페이스를 정의한 뒤, 각 구현체를 정의하기 전에 공통적으로 사용될 로직들을 추상 클래스에 중앙화하고 세부적인 구현을 템플릿 메서드에 맡기는 코드 구조를 즐겨 사용함 

AbstractPlatformTransactionManager도 이러한 코드 구조를 가지는 기반 클래스로, 스프링의 표준 트랜잭션 동작 흐름들을 처리함
- 기존 트랜잭션이 있는지 확인
- 적절한 트랜잭션 전파 행동 적용
- 필요한 경우 트랜잭션 재개 또는 중단
- 커밋 시 rollback-only 플래그 여부 확인
- 롤백 시 rollback-only 적용 또는 실제 롤백 수행
- 등록된 동기화 콜백 트리거 (트랜잭션 동기화가 활성화된 경우)

### 상속 관계

PlatformTransactionManager 및 ConfigurableTransactionManager 확장

ConfigurableTransactionManager: TransactionExecutionListener 등록 설정

```java
public abstract class AbstractPlatformTransactionManager
		implements PlatformTransactionManager, ConfigurableTransactionManager, Serializable {
```

### 멤버 필드

```java
// 트랜잭션 동기화 상수값
public static final int SYNCHRONIZATION_ALWAYS = 0;
public static final int SYNCHRONIZATION_ON_ACTUAL_TRANSACTION = 1;
public static final int SYNCHRONIZATION_NEVER = 2;

static final Map<String, Integer> constants = Map.of(
        "SYNCHRONIZATION_ALWAYS", SYNCHRONIZATION_ALWAYS,
        "SYNCHRONIZATION_ON_ACTUAL_TRANSACTION", SYNCHRONIZATION_ON_ACTUAL_TRANSACTION,
        "SYNCHRONIZATION_NEVER", SYNCHRONIZATION_NEVER
);

private int transactionSynchronization = SYNCHRONIZATION_ALWAYS;

// TransactionDefinition.TIMEOUT_DEFAULT = -1
private int defaultTimeout = TransactionDefinition.TIMEOUT_DEFAULT;

// 트랜잭션 중첩 허용 여부
private boolean nestedTransactionAllowed = false;

// 기존 트랜잭션 검증 여부
private boolean validateExistingTransaction = false;

// 트랜잭션 실패 시 전체 트랜잭션 롤백 실행 여부
private boolean globalRollbackOnParticipationFailure = true;

// globalRollbackOnly 상태로 설정된 경우 커밋 시도 전 예외 발생 여부
private boolean failEarlyOnGlobalRollbackOnly = false;

// 커밋 실패 시 롤백 여부
private boolean rollbackOnCommitFailure = false;

// transactionExecutionListener 관리
private Collection<TransactionExecutionListener> transactionExecutionListeners = new ArrayList<>();
```

### PlatformTransactionManager 구현 메서드

친절하게 주석으로 PlatformTransactionManager의 구현부라는 것을 표시해준 옛날 스프링 코드의 모습

```java
//---------------------------------------------------------------------
// Implementation of PlatformTransactionManager
//---------------------------------------------------------------------
```

#### AbstractPlatformTransactionManager - public final getTransaction(TransactionDefinition definition)

getTransaction 메서드는 트랜잭션 전파 설정에 따라 트랜잭션을 처리함

[TransactionDefinition](#transactiondefinition) 파라미터: 트랜잭션 설정 정보 보관 객체
- 트랜잭션 이름, 트랜잭션 전파
- 격리 수준, 시간 초과, 읽기 전용

```java
@Override
public final TransactionStatus getTransaction(@Nullable TransactionDefinition definition)
        throws TransactionException {
    
    // 주어진 TransactionDefinition 또는 기본(모든 값 설정 X) 사용
    TransactionDefinition def = (definition != null ? definition : TransactionDefinition.withDefaults());

    // doGetTransaction 템플릿 메서드를 통해 트랜잭션 객체 획득 
    Object transaction = doGetTransaction();

    /*
        isExistingTransaction 템플릿 메서드를 통해 doGetTransaction 메서드에서 
        획득한 트랜잭션이 새로운 트랜잭션이 아닌 이미 존재한 트랜잭션인지 확인
     */
    if (isExistingTransaction(transaction)) {
        // 만약 이미 존재한 트랜잭션(트랜잭션이 이미 시작된 경우)이라면
        // 설정값에 따른 트랜잭션 전파 처리 후 결과(TransactionStatus) 반환
        return handleExistingTransaction(def, transaction, debugEnabled);
    }

    // 새로운 트랜잭션인 경우 트랜잭션 설정 값 검증
    if (def.getTimeout() < TransactionDefinition.TIMEOUT_DEFAULT) {
        throw new InvalidTimeoutException("Invalid transaction timeout", def.getTimeout());
    }

    // 새로운 트랜잭션의 트랜잭션 전파 설정 값이 필수인 경우 예외 발생
    if (def.getPropagationBehavior() == TransactionDefinition.PROPAGATION_MANDATORY) {
        throw new IllegalTransactionStateException(
                "No existing transaction found for transaction marked with propagation 'mandatory'");
    }
    // 새로운 트랜잭션의 트랜잭션 전파 설정 값이 REQUIRED, REQUIRES_NEW, NESTED인 경우
    else if (def.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRED ||
            def.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW ||
            def.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
        
        // 현재 활성화된 트랜잭션 동기화 리소스들이 있는 경우 중단 시킴
        SuspendedResourcesHolder suspendedResources = suspend(null);
        
        try {
            // 새로운 트랜잭션 시작 후 결과(TransactionStatus) 반환
            return startTransaction(def, transaction, false, debugEnabled, suspendedResources);
        }
        catch (RuntimeException | Error ex) {
            // 예외 발생 시 기존 트랜잭션 재개
            resume(null, suspendedResources);
            throw ex;
        }
    }
    else {
        // 기타 트랜잭션 전파 설정 값인 경우 비어있는 트랜잭션을 생성함
        // 실제 트랜잭션은 아니지만, 잠재적으로 트랜잭션 동기화됨
        boolean newSynchronization = (getTransactionSynchronization() == SYNCHRONIZATION_ALWAYS);
        return prepareTransactionStatus(def, null, true, newSynchronization, debugEnabled, null);
    }
    
}
```

#### getTransaction 메서드에서 내부적으로 호출하는 메서드

1. `TransactionStatus handleExistingTransaction(...)`

getTransaction 메서드에서 트랜잭션 생성을 처리하기 전에 기존 트랜잭션이 있는지 확인하는데, 있는 경우 이 메서드를 호출함

handleExistingTransaction 메서드는 트랜잭션 전파 설정 값에 따른 TransactionStatus 객체를 생성하여 반환함

##### handleExistingTransaction - PROPAGATION_NEVER

TransactionDefinition.PROPAGATION_NEVER인 경우 예외 발생

```java
if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NEVER) {
  throw new IllegalTransactionStateException(
      "Existing transaction found for transaction marked with propagation 'never'");
}
```

##### handleExistingTransaction - PROPAGATION_NOT_SUPPORTED

TransactionDefinition.PROPAGATION_NOT_SUPPORTED는 기존 트랜잭션이 있는 경우 기존 트랜잭션을 보류하고, 트랜잭션 없이 작업을 바로 수행함 

```java
if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NOT_SUPPORTED) {
  Object suspendedResources = suspend(transaction);

  boolean newSynchronization = (getTransactionSynchronization() == SYNCHRONIZATION_ALWAYS);

  return prepareTransactionStatus(definition, null, false, newSynchronization, debugEnabled, suspendedResources);
}
```

##### 트랜잭션 전파 설정: PROPAGATION_NOT_SUPPORTED

TransactionDefinition.PROPAGATION_REQUIRES_NEW는 기존 트랜잭션을 보류하고 새 트랜잭션을 생성함

```java
if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW) {
    SuspendedResourcesHolder suspendedResources = suspend(transaction);
    try {
        return startTransaction(definition, transaction, false, debugEnabled, suspendedResources);
    }
    catch (RuntimeException | Error beginEx) {
        resumeAfterBeginException(transaction, suspendedResources, beginEx);
        throw beginEx;
    }
}
```

##### 트랜잭션 전파 설정: PROPAGATION_NESTED

TransactionDefinition.PROPAGATION_NESTED는 기존 트랜잭션이 없으면 새 트랜잭션을 만들고, 있으면 SavePoint를 이용하여 중첩 트랜잭션을 생성함

중첩 트랜잭션을 지원하지 않는 트랜잭션 매니저 구현체라면 NestedTransactionNotSupportedException 발생

```java
if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
    if (!isNestedTransactionAllowed()) {
        throw new NestedTransactionNotSupportedException(
                "Transaction manager does not allow nested transactions by default - " +
                "specify 'nestedTransactionAllowed' property with value 'true'");
    }
    
    /*
        useSavepointForNestedTransaction()의 기본 반환 값: true
        
        SavePoint 기능을 사용하는 트랜잭션 매니저 구현체인 경우
        스프링이 관리하고 있는 트랜잭션 내에서 SavePointManager API를 구현한 TransactionStatus를 통해 saveppoint를 생성함
        일반적으로 JDBC savepoint 사용하며, 스프링 동기화를 활성화하지 않음
     */
    if (useSavepointForNestedTransaction()) {
        DefaultTransactionStatus status = newTransactionStatus(
                definition, transaction, false, false, true, debugEnabled, null);
        this.transactionExecutionListeners.forEach(listener -> listener.beforeBegin(status));
        try {
            status.createAndHoldSavepoint();
        }
        catch (RuntimeException | Error ex) {
            this.transactionExecutionListeners.forEach(listener -> listener.afterBegin(status, ex));
            throw ex;
        }
        this.transactionExecutionListeners.forEach(listener -> listener.afterBegin(status, null));
        return status;
    }
    
    /*
       SavePoint 기능을 사용하지 않는 트랜잭션 매니저 구현체인 경우
       SavePoint 없이 중첩된 begin, commit/rollback 호출을 통한 중첩 트랜잭션 생성
       일반적으로 JTA에만 해당되며 기존 JTA 트랜잭션의 경우 여기서 스프링 동기화가 활성화될 수 있음
     */
    else {
        return startTransaction(definition, transaction, true, debugEnabled, null);
    }
}
```

##### handleExistingTransaction - PROPAGATION_REQUIRED, PROPAGATION_SUPPORTS, PROPAGATION_MANDATORY

기존 트랜잭션의 검증이 필요한 트랜잭션 매니저 구현체의 경우 두 가지를 확인함
- 기존 트랜잭션의 격리 수준과 새로 생성할 트랜잭션의 격리 수준이 다른지
- 기존 트랜잭션의 읽기 전용 설정 값과 새로 생성할 트랜잭션의 읽기 전용 설정 값이 다른지

트랜잭션 매니저 구현체의 트랜잭션 매니저 동기화 설정 값에 따라 트랜잭션 생성 

```java
if (isValidateExistingTransaction()) {
    if (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {
        Integer currentIsolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
        if (currentIsolationLevel == null || currentIsolationLevel != definition.getIsolationLevel()) {
            throw new IllegalTransactionStateException("Participating transaction with definition [" +
                    definition + "] specifies isolation level which is incompatible with existing transaction: " +
                    (currentIsolationLevel != null ?
                            DefaultTransactionDefinition.getIsolationLevelName(currentIsolationLevel) :
                            "(unknown)"));
        }
    }
    if (!definition.isReadOnly()) {
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            throw new IllegalTransactionStateException("Participating transaction with definition [" +
                    definition + "] is not marked as read-only but existing transaction is");
        }
    }
}
boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
return prepareTransactionStatus(definition, transaction, false, newSynchronization, debugEnabled, null);
```

2. `TransactionStatus startTransaction(...)`

getTransaction 또는 handleExistingTransaction 메서드에서 새로운 트랜잭션을 시작할 때 호출하는 메서드

동작 흐름
- 트랜잭션 매니저 구현체의 트랜잭션 동기화 설정 값에 따라 새 트랜잭션 생성
- 트랜잭션 시작 전처리
- 트랜잭션 시작
- 트랜잭션 시작 후처리 

```java
private TransactionStatus startTransaction(TransactionDefinition definition, Object transaction,
			boolean nested, boolean debugEnabled, @Nullable SuspendedResourcesHolder suspendedResources) {

    boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
    DefaultTransactionStatus status = newTransactionStatus(
            definition, transaction, true, newSynchronization, nested, debugEnabled, suspendedResources);
    this.transactionExecutionListeners.forEach(listener -> listener.beforeBegin(status));
    try {
        doBegin(transaction, definition);
    }
    catch (RuntimeException | Error ex) {
        this.transactionExecutionListeners.forEach(listener -> listener.afterBegin(status, ex));
        throw ex;
    }
    prepareSynchronization(status, definition);
    this.transactionExecutionListeners.forEach(listener -> listener.afterBegin(status, null));
    return status;
}
```

### 템플릿 메서드

자식 클래스에서 구현해야 할 템플릿 메서드 정의 부분도 친절하게 주석으로 표시해줌

```java
//---------------------------------------------------------------------
// Template methods to be implemented in subclasses
//---------------------------------------------------------------------
```

## JpaTransactionManager

## TransactionSynchronizationManager

## Spring Transaction Propagation

## Transaction Isolation Level

## @Transactional

## Query Method Transaction

## Rollback Rules

## DataSource, Connection Pooling, Spring Transaction Context