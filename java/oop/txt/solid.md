[SOLID](#solid)

[Single Responsibility Principal, SRP](#single-responsibility-principal-srp)

[Open-Closed Principal, OCP](#open-closed-principal-ocp)

[Liskov Substitution Principal, LSP](#liskov-substitution-principal-lsp)

## SOLID

로버트 C. 마틴(Uncle Bob)가 설립한 객체지향 설계 원칙으로, 각 원칙의 맨 앞 글자를 따서 SOLID 원칙이라고 한다

객체지향이 가진 장점인 유연성(flexiable), 유지보수성(maintainable), 가독성(understandable)을 발휘할 수 있는 객체 설계 방법을 제안한다

## Single Responsibility Principal, SRP

**엔티티/모듈(클래스, 메서드, 라이브러리 등)은 단 하나의 책임을 가져야 한다**는 원칙 (또는 변경해야 할 이유가 한 개보다 많으면 안된다)

로버트 C. 마틴은 책임이란 "변경의 이유"라고 정의한다

같은 이유로 변경해야 할 것들은 함께 모으고, 다른 이유로 변경해야 할 것들은 분리해야 한다는 의미이다 (관심사 분리)

만약 만약 특정 모듈이 여러 책임을 지니고 있으면 요구사항에 따라 여러 이유에 의해 변경될 것이고 의도치 않은 부작용이 발생할 가능성이 높아진다

**여러 책임을 가진 클래스**

```java
class Developer {
    
    public void work() {
        meeting();
        writingCode();
        makingCoffee();
    } 
}
```

**SRP를 준수한 객체 설계**

```java
class Developer {
    
    public void writingCode() {}
}

class Manager {
    
    public void meeting() {}
}

class Barista {
    
    public void makingCoffee() {}
}
```

## Open-Closed Principal, OCP

**엔티티는 확장에는 열려있어야 하고, 수정에는 닫혀야 한다**는 원칙

개발자는 기존 코드를 그대로 유지하면서 새로운 기능을 추가할 수 있어야 한다는 의미이다

OCP를 준수하지만 SRP를 준수하지 않는 경우 코드 변경 없이 확장할 수 있지만 클래스가 여러 책임을 떠안아 코드의 복잡도를 높인다

또한 모듈의 책임이 과도하게 집중되는 것은 변경이 다른 부분에 의도치 않게 영향을 미치는 부작용을 초래할 수 있다

```java
class Developer {
    
    private FrontendCode frontendCode;
    private BackendCode backendCode;
    private InfraCode infraCode;
    
    public void work(String part) {
        if (part.equals("frontend")) {
            developFrontend();
        }
        else if (part.equals("backend")) {
            developBackend();
        }
        else if (part.equals("infra")) {
            developInfra();
        }
    }
    
    private void developFrontend() {}
    private void developBackend() {}
    private void developInfra() {}
}
```

위의 코드는 새로운 개발 파트를 추가할 때 기존 메서드를 수정하지 않고, 조건문에 새로운 개발 메서드만 추가하면 된다

하지만 특정 Developer 클래스가 프론트엔드, 백엔드, 인프라 등 모든 파트의 책임을 가지고 있기에 여러 가지 변경 이유가 존재하고, 각 파트가 로직과 강하게 결합되어 한 부분의 변경이 다른 부분에 영향을 미칠 수 있다

**OCP와 SRP를 함께 준수한 코드**

```java
interface Developer {
    void work();
}

class FrontendDeveloper implements Developer {

    private FrontendCode frontendCode;

    public void work() {
        System.out.println("developing frontend code ...");
    }
}

class BackendDeveloper implements Developer {

    private BackendCode backendCode;

    public void work() {
        System.out.println("developing backend code ...");
    }
}

class InfraDeveloper implements Developer {

    private Map<String, Infrastructure> infrastructureMap;
    private InfraCode infraCode;

    public void work() {
        System.out.println("developing infra code ...");
    }
}
```

OCP와 SRP를 함께 준수하면 객체의 책임 분리를 통해 코드가 단순해지면서 확장에도 유연해진다

### OCP 준수 방법

#### OCP 준수 첫 번째 방법: 인터페이스와 다형성 활용, 전략 패턴

인터페이스나 추상 클래스를 통해 핵심 로직을 정의하고, 구체적인 구현은 이를 상속받아서 확장하는 방법이다

인터페이스 구현체를 생성하면 기존 코드를 수정하지 않고 새로운 기능을 추가할 수 있다

전략 패턴은 동적으로 객체의 행동(알고리즘)을 변경할 수 있는 디자인 패턴으로 다형성을 이용할 수 있는 방법 중 하나이다

**다형성 + 전략 패턴 코드**

```java
/* ------- PaymentStrategy 정의 ------- */

interface PaymentStrategy {
    void pay(int amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("using credit card");
    }
}

class BitcoinPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("using bitcoin");
    }
}

/* ------- PaymentStrategy 사용 -------- */

class PaymentService {
    
    private PaymentStrategy strategy;
    
    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void processPayment(int amount) {
        strategy.pay(amount);
    }
}
```

#### OCP 준수 두 번째 방법: 컨테이너 기반 개발, IoC/DI

IoC 컨테이너를 사용하여 의존성을 주입하면 OCP를 준수하기 쉬워진다

**스프링의 DI 코드**

```java
public interface NotificationService {
    void notify(String message);
}

@Service
class EmailNotificationService implements NotificationService {
    public void notify(String message) {
        System.out.println("sending email");
    }
}

@Service
class SmsNotificationService implements NotificationService {
    public void notify(String message) {
        System.out.println("sending sms");
    }
}

@Contorller
class NotificationController {
    
    private final NotificationService notificationService;
    
    public NotificationService(@Qualifier("smsNotificationService") NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
```

#### OCP 준수 세 번째 방법: 플러그인 아키텍처

시스템의 핵심 로직과 확장 기능을 분리하여 플러그인을 통해 동적으로 확장할 수 있는 아키텍처 설계 방법이다

#### OCP 준수 네 번째 방법: 람다식

자바 8부터 제공하는 함수형 인터페이스와 람다를 사용하여 동적으로 행동을 정의할 수 있다

```java
public class JavaDeveloper {
    
    public void doTask(Supplier<String> problem) {
        String unsolvedProblem = problem.get();
        coding(unsolvedProblem);
    }
}

JavaDeveloper developer = new JavaDeveloper();
developer.doTask(() -> "oop concept");
developer.doTask(() -> "solid principal understanding");
```

### Liskov Substitution Principal, LSP

