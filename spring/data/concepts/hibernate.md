하이버네이트6.6 final 기준

## Hibernate

하이버네이트는 개발자가 SQL 쿼리 대신 자바 코드를 통해 관계형 데이터를 자연스럽고 타입 세이프하게 다룰 수 있도록 ORM 기능을 제공해주는 라이브러리이자 JPA의 구현체임

ORM 목표: 취약하고 타입 세이프하지 않은 코드를 제거하고, 장기적으로 대규모 프로그램을 더 쉽게 유지 관리할 수 있도록 하는 것

Hibernate API는 세 가지 요소로 구성됨
- JPA API 구현 (EntityManagerFactory, EntityManager 인터페이스 및 JPA의 O/R 매핑 어노테이션)
- SessionFactory와 Session을 중심으로 한 하이버네이트 네이티브 API
- 하이버네이트에서 제공하는 매핑 어노테이션

아래의 애플리케이션 코드와 JPA/하이버네이트 ORM 간의 구조를 보면 매핑 어노테이션으로 엔티티 클래스를 나타내고, API를 통해서 퍼시스턴스 작업을 수행함

<img src="./images/hibernate-api-overview.png" alt="hibernate api overview" style="width: 40%; height:40%; border-radius: 10px">

퍼시스턴스 작업
- 트랜잭션, 세션 관리
- 하이버네이트 세션을 통한 데이버테이스와의 상호작용
- 필요한 데이터 페치 및 준비
- 실패 처리


