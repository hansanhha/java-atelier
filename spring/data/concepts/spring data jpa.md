spring boot 3.3.2 기준

[Spring Data Project](#spring-data-project)
- [주요 기능](#주요-기능)
- [메인 모듈](#메인-모듈)

## Spring Data Project

[Spring Data](https://spring.io/projects/spring-data)

스프링 데이터는 기본 데이터 저장소의 특수성을 유지하면서 데이터 접근을 위한 일관성 있는 스프링 기반 프로그래밍 모델을 제공함

관계형, 비관계형 데이터베이스, 데이터 접근 기술, 클라우드 기반 데이터 서비스, 맵 리듀스 프레임워크를 쉽게 사용할 수 있음

스프링 데이터 프로젝트는 특정 데이터베이스에 특화된 하위 프로젝트를 포함하는 포괄적인 프로젝트임

### 주요 기능

#### 리포지토리 패턴, 커스텀 객체 매핑 추상화
- 기본적인 CRUD 작업을 쉽게 처리할 수 있도록 추상화 제공
- 커스텀 객체 매핑도 지원
- ```java
  public interface UserRepository extends JpaRepository<User, Long> {}
  ```

#### 리포지토리 메서드 이름에서 동적 쿼리 파생
- 인터페이스의 메서드 이름을 기반으로 쿼리를 자동으로 생성함
- SQL을 직접 작성할 필요없이 쉽게 데이터 작업 가능
- ```java
  public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmailAndName(String email, String name);
  }
  ```

#### 기본 속성을 제공하는 구현 도메인 베이스 클래스
- 기본적인 속성을 제공하는 베이스 클래스를 통해 도메인 클래스의 반복 코드를 줄여줌
- ```java
  // 기본 속성 제공 베이스 클래스
  @MappedSuperclass
  @EntityListeners(AuditingEntityListener.class)
  public abstract class BaseEntity {
  
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
  }

  // 상속을 통해 기본 속성을 제공받음  
  @Entity
  public class User extends BaseEntity {
    private String name;
    private String email;
  }
  ```

#### auditing 지원(created, last changed 등)
- 엔티티 생성 및 수정 시간 등을 자동으로 기록하는 감사(auditing) 기능 제공
- ```java
  @Configuration
  @EnableJpaAuditing
  public class AuditConfig {
    Timestamp createdDate;
    Timestamp lastModifiedDate;
  }
  ```

#### 커스텀 리포지토리 코드 통합
- 기본 리포지토리 기능 외에도, 커스텀 리포지토리를 만들어 기존 리포지토리에 쉽게 통합할 수 있음
- ```java
  public interface UserRepositoryCustom {
    List<User> customLogic();
  }
  
  public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;
  
    @Override
    public List<User> customLogic() {
       ...
    }
  }
  
  public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustomImpl {
  }
  ```

#### JavaConfig 및 커스텀 XML 네임스페이스를 통한 스프링 통합
- JavaConfig를 통해 DataSource, 트랜잭션 매니저 등을 설정할 수 있음

#### Spring 컨트롤러와의 통합

### 메인 모듈

#### Spring Data Commons

모든 스프링 데이터 모듈의 기반이 되는 핵심 스프링 개념

#### Spring Data JDBC

JDBC에 대한 스프링 데이터 리포지토리

#### Spring Data JPA

JPA에 대한 스프링 데이터 리포지토리

#### Spring Data Redis

스프링 애플리케이션에서 간단하게 설정하여 레디스에 접근할 수 있는 모듈

## Spring Data Common

## Spring Data JPA