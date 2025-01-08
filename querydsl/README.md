[Querydsl-JPA](#querydsl-jpa)

[Querydsl-JPA 설정](#querydsl-jpa-설정)

[Q 클래스 생성](#q-클래스-생성)

## Querydsl-JPA

JPA 2.0 스펙부터 추가된 Specification를 통해 동적 쿼리를 만드려면 JPA Static Metamodel을 만들어야 한다

메타모델은 엔티티의 각 속성을 정적 필드로 제공하며, 이를 통해 타입 안전한 방식으로 동적 쿼리를 만들 수 있다

```java
public class BookSpecifications {
    
    public static Specification<Book> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                category == null ? null : criteriaBuilder.equal(root.get("mainCategory"), category);
    }
}

public class BookService {

    public List<Book> findBooks(String category) {
        Specification<Book> spec = Specification
                .where(BookSpecifications.hasCategory(category));

        return bookRepository.findAll(spec);
    }
}
```

JPA Specification은 쿼리를 정의하기 위해 CriteriaBuilder를 사용해야 하므로 코드가 다소 복잡하고 읽기 어렵다

또한 문자열로 컬럼 이름을 다룰 때 컴파일 타임에 오류를 검출하기 어렵다

querydsl은 이러한 문제를 해결하는 라이브러리로 JPA static Metamodel의 기능을 발전시켜 간결하고 읽기 쉬운 코드로 동적 쿼리를 작성할 수 있게 도와준다

## Querydsl-JPA 설정

#### querydsl 의존성 추가 (build.gradle.kts)

querydsl apt(annotation processing tool)를 통해 컴파일 과정에서 jakarta 어노테이션을 인식하여 Q 클래스 메타모델을 자동 생성한다

스프링 부트 3.0 버전부터 java ee 의존성을 jakarta 패키지로 변경함에 따라 querydsl도 그에 맞춰서 jakarta로 설정해야 한다

```kotlin
implementation("com.querydsl:querydsl-jpa:${version}:jakarta")
annotationProcessor("com.querydsl:querydsl-apt:${version}:jakarta")
annotationProcessor("jakarta.annotation:jakarta.annotation-api")
annotationProcessor("jakarta.persistence:jakarta.persistence-api")
```

#### gradle configurations 설정

어노테이션 처리기가 컴파일 시점에 동작할 수 있도록 설정한다

```kotlin
configurations {
    compileOnly {
        extendsFrom(
            configurations.annotationProcessor.get()
        )
    }
}
```

## Q 클래스 생성

#### 엔티티 작성

```java
@Entity
public class Book extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private UUID isbn;

    private String title;

    private String author;
}
```

#### 컴파일

gradle의 compileJava 또는 mvn compile 명령을 통해 클래스를 컴파일하면 빌드 디렉토리에 Q 클래스가 생성된다

```shell
./gradlew compileJava
```

![q class](./assets/querydsl%20qclass.png)
