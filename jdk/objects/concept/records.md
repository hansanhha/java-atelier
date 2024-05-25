[JEP 395](https://openjdk.org/jeps/395)

[oracle docs](https://docs.oracle.com/en/java/javase/17/language/records.html)

## Record

데이터 집합, 불변 데이터 모델링을 돕는 특수 클래스

```
record Rectangle(double length, double width) {} 
```

구성
- record 이름 : Rectangle
- type paramenter(optional)
- header(component list) : length, width
    - header에 명시된 각 component마다 동일한 이름과 타입으로 private final 필드를 가짐
    - 또한 동일한 타입을 반환하는 동일한 이름의 public accessor 메서드도 가짐(length(), width())
- body

### compact constructor

normal constructor 

```
record Rectangle(double length, double width) {

    public Rectangle(double length, double width) {
        if (length < 5 || width < 5) {
            throw new IllegalArgumentException();
        }

        this.length = length;
        this.width  = width;
    }
}
```

compact constructor

```
record Rectangle(double length, doule width) {
    public Rectangle {
        if (length < 5 || width < 5) {
            throw new IllegalArgumentException();
        }
    }
}
```

매개변수, 할당문 생략

생성자 마지막 부분에서 매개변수가 field에 암시적으로 할당됨

### 선언 규칙

record 내 선언 가능한 것
- static field, static initializer, static method
- instance method
- nested class, interface, record

불가능한 것
- instance field, instance initializer, native method

## 특징

모든 Record 타입은 java.lang.Record를 상속함

accessor, constructor, equals, hashcode, toString 메서드 자동 생성
    - 자동 생성되는 생성자 매개변수는 header와 동일함
    - new 키워드로 인스턴스 생성 가능
    - 자동 생성되는 메서드를 직접 구현하는 경우 동일한 특성(이름, 반환 타입 등)을 갖도록 강제함

record 클래스는 암시적으로 final이므로 다른 record를 명시적으로 extends 할 수 없음

generic record 생성 가능

interface implmenents 가능

각 component에 annotate 가능
- 선언한 annotation의 @Target에 따라 멤버와 생성자에 전파됨

Record라는 이름의 클래스를 만들고 import 경로에 클래스명을 포함하여 명시하지 않는 경우 컴파일 에러 발생
- java 파일은 암시적으로 import java.lang.* 문이 붙음
- com.example.* 으로 커스텀 Record 클래스를 만들고 사용하는 경우 컴파일러가 어느 Record 클래스를 가져와야되는지 모르기 때문에 컴파일 에러 발생
- com.exmaple.Record와 같이 구체적인 클래스명을 적어주면 됨




