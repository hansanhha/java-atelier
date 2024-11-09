### Class (Top-level class)

java file은 모두 java.lang 패키지를 자동으로 import함

Public Class
- 하나의 소스 파일에는 하나의 public class만 있을 수 있음
    - 아예 없어도 됨
- 파일명과 클래스명 일치
- 다른 패키지에서 접근 가능

```
public class Number {
}
```

Non-Public class 
- 같은 패키지에서만 접근 가능
- 하나의 소스 파일에 여러 개 포함 가능
```
class Format {
}

public class Number {
}
```

### Static inner Class

static 키워드를 포함한 내부 클래스 

외부 클래스의 인스턴스에 종속적이지 않음(외부 클래스 없이 생성 가능)

외부 클래스의 static 멤버에만 접근 가능

```java
class Number {

    static Number staticOuterNumber;

    static class Calculator {
        public Number add(double y) {
            return staticOuterNumber + y; // 외부 클래스 static 멤버에 접근 가능
        }
    }
}

Caculator calculator = new Calcaultor(); // 외부 클래스 인스턴스 없이 생성 가능

calculator.operate(1,3); 
```

### Non-Static inner Class

외부 클래스의 인스턴스에 종속적(외부 클래스를 통해 생성 가능)

외부 클래스의 멤버에 직접 접근 가능

```java
class Number {

    Number outerNumber;

    class Printer {
        public void print() {
            System.out.println(outerNumber);
        }
    }
}

Number number = new Number();

Printer printer = new number.Printer(); // 외부 클래스 인스턴스를 통해 생성

printer.print();
```

### Local Class

메서드 내부에 정의된 클래스로 메서드가 실행됐을 때 인스턴스화 가능

메서드 내의 변수에 접근하기 위한 목적

### Abstract Class

하나 이상의 추상 메서드를 포함하거나 class 정의에 abstract 키워드를 붙인 클래스

직접 인스턴스화 불가

상속을 통해 자식 클래스에서 추상 메서드 구현 필요

### Interface

모든 메서드(default, static 제외)가 추상 메서드인 특수 클래스 - public abstract 생략 가능

default 메서드 : 메서드 기본 구현 제공(구현 강제 X), 오버라이딩 가능, 구현 클래스 인스턴스를 통해 호출 가능 

static 메서드 : 구현 클래스없이 호출 가능, 오버라이딩 불가능, 메서드 숨김(method hiding) 가능
- method hiding : 서브 클래스가 슈퍼 클래스의 static 메서드와 같은 시그니처의 static 메서드를 정의했을 때 발생함(컴파일 시점), 참조 변수의 타입에 따라 호출되는 메서드 결정 

### Anonymous Class

이름 없이 선언과 동시에 객체가 생성되는 클래스

일회성, 콜백, 이벤트 리스너 목적으로 사용

### Functional Interface

오직 하나의 추상 메서드만 정의되어 있는 인터페이스

@FunctionalInterface로 함수형 인터페이스 표시

java.util.function에 미리 정의되어 있음

다음 목적으로 사용 가능
- 동작 파라미터화
    - 메서드에 전달되는 파라미터를 통해 메서드 동작을 동적으로 변경할 수 있는 기법
    - 함수형 인터페이스를 메서드 파라미터로 받으면, 동작 자체를 파라미터로 전달 가능
    - 람다식을 통해 간단하게 구현할 수 있음
- 람다식
    - 익명 함수를 정의하는 방법
    - 함수형 인터페이스 인스턴스를 쉽게 생성
    - 동작 파라미터화, 고차 함수 패턴 구현 시 사용됨
    - 고차 함수 : 함수를 파라미터로 받거나 결과로 반환하는 함수(함수를 값으로 취급)
