## Fundamental Concepts

spring modulith : 스프링 부트 애플리케이션에서 프로젝트 구조를 논리적인 모듈로 구성할 수 있게 도와줌

기능
- 애플리케이션 구조 검증
- 모듈 의존성 문서화
- 개별/통합 모듈 테스트
- 런타임 모듈 상호작용 observe
- 느슨한 결합 모듈 상호작용 구현

Application Modules
- module은 3가지 단위로 구성됨
  - provided interface : 노출할 API(다른 모듈에 노출할 스프링 빈, 모듈에 의해 발행되는 이벤트)
  - internal implementation component : 모듈 내부에서만 사용하는 컴포넌트 
  - required interface : 참조하는 API(의존성 주입받을 다른 모듈의 스프링 빈, 수신할 이벤트, 사용할 프로퍼티)

Application Module 구조
```
- src/main/java
    - shop (root package)
        - @SpringBootApplication
        - order(module package)
            - package-info.java
            - OrderManagement
            - OrderInternal
            - events (package)
                -
                        - package-info.java
        - inventory(other module package)
            - ...
```

module 패키지는 API 패키지로 취급 - 외부에서 참조 가능

module 하위 패키지는 internal로 취급 - 외부에서 참조 불가능
- 스프링 modulith에서 접근을 막는 거라서 컴파일러는 에러를 잡지 못함
- 잘못된 접근은 애플리케이션 로드 시점에 에러 발생

## Application Module 의존성 명시

package-info.java에 @ApplicationModule을 사용하여 접근할 의존성을 명시

```java
@org.springframework.modulith.ApplicationModule(
        allowedDependencies = "order"
)
package com.hansanhha.spring.shop.inventory;
```

위의 설정으로 inventory 모듈은 order 모듈만 접근 가능

## NamedInterface

특정 module 하위 패키지를 API로 제공할 때 사용

packgae-info.java에 @NamedInterface 사용

```java
@org.springframework.modulith.NamedInterface("events")
package com.hansanhha.spring.shop.order.events;
```

다른 모듈의 package-info.java에 공개한 API 명시
```java
@org.springframework.modulith.ApplicationModule(
        allowedDependencies = "order::events"
)
package com.hansanhha.spring.shop.inventory;
```

## Application Event

이벤트 발행-소비를 통해 모듈 간 상호작용을 분리

이벤트 기반 동작
- 각 모듈에서 이벤트 정의
- 스프링 ApplicationEventPublisher으로 정의한 이벤트 발행
- 다른 모듈에서 @ApplicationModuleListener를 통해 이벤트 수신 및 로직 실행

