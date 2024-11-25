[Generics](#generics)

[Type Erasure](#type-erasure)

[Type Bounds: Covariance, Contravariance, Invariance](#type-bounds-covariance-contravariance-invariance)

[Type Inference](#type-inference)

[Parameterized Types](#parameterized-types)

[Type Variables](#type-variables)

[Generic Method](#generic-method)

[Type Arguments](#type-arguments)

[java.lang.reflect.Type](#javalangreflecttype)

## Generics

도입: java se 5 (2004)

타입 안정성 및 타입 변환을 최소화하기 위해 클래스, 인터페이스, 메서드에 타입을 매개변수로 사용할 수 있도록 하는 게 제네릭임

일반적으로 매개변수라 하면 메서드의 파라미터를 생각하게 됨

```java
public class Bank {
    private int dollar;
    
    public void add(int dollar) {
        if (dollar <= 0) {
            return;
        }
        
        this.dollar += dollar;
    }
}
```

위의 Bank 클래스의 add 메서드는 고정된 int 파라미터에 한해서 값을 받음

```java
public class Bank<Money> {
    private Money money;

    public void add(Money money) {
        money.add(money);
    }
}

public class Dollar {
    // ...
}

public class Won {
    // ...
}

Bank<Dollar> Fed = new Bank<>();
Bank<Won> BankOfKorea = new Bank<>();
```

타입 매개변수는 타입 자체를 매개변수로 받음

위의 Bank 클래스는 Money(임의의 이름) 타입 매개변수를 통해 하나의 필드가 여러 타입을 참조할 수 있음 

제네릭은 다음과 같은 여러 장점을 제공함

#### 타입 안정성

자바는 컴파일 시점에 타입 검사를 통해 제네릭 클래스나 제네릭 메서드에서 다룰 타입을 명확히 지정함

제네릭 규칙에 위반되는 코드가 있다면 컴파일 오류를 발생시켜서 런타임에 발생할 수 있는 `ClassCastException` 오류를 방지함

#### 재사용성

제네릭은 여러 데이터 타입에 대해 동일한 로직을 제공함

두 개의 객체를 비교하는 세부적인 구현은 객체마다 다르겠지만, 비교 행위 자체는 빈번하게 발생함

```java
@FunctionalInterface
public interface Comparator<T> {
    int compare(T o1, T o2);
}
```

자바는 Comparator 함수형 인터페이스에 타입 매개변수를 적용한 두 객체 간의 비교를 추상화함

#### 가독성 향상

컴파일 시점에 타입 검사를 하는 제네릭의 특징에 의해, 컴파일러가 타입을 알고 있기 때문에 런타임에 별도의 타입 캐스팅이 필요하지 않음

또한 자바는 자동 언박싱/박싱을 지원하기 때문에 기본 타입과 객체 간의 변환도 간소화됨

```java
// 제네릭을 사용하지 않은 코드
public class WithoutGenerics {
    
    public static void main(String[] args) {
        
        // 타입 매개변수 지정 X
        List rawList = new ArrayList();

        // String, Integer 타입 등 서로 다른 타입의 객체 혼합 가능
        rawList.add("Hello");
        rawList.add(1234);

        // List에 어느 타입이 들어간지 런타임에 알 수 없기 때문에
        // 요소를 꺼낼 때 타입 캐스팅이 명시적으로 필요함
        String first = (String) rawList.get(0);
        System.out.println(first);

        // 두 번째 넣은 요소는 Integer 타입이기 때문에
        // ClassCastException 발생
        String second = (String)rawList.get(1);
    }
}
```

```java
// 제네릭을 사용한 코드 
public class WithGenerics {

    public static void main(String[] args) {
        
        // String 타입 매개변수 지정
        List<String> genericsList = new ArrayList<>();
        genericsList.add("Hello");
        
        // 타입 매개변수로 String을 지정했으므로 다른 타입을 허용하지 않음
        genericsList.add(1234); // 컴파일 오류 발생
        
        // 제네릭으로 인해 컴파일러가 타입을 알고 있기 때문에 
        // 자동 타입 캐스팅이 지원됨
        String first = genericsList.get(0);
        
        // 컴파일러의 타입 캐스팅 코드 자동 삽입
        // String first = (String) genericsList.get(0);
    }
}
```

## Type Erasure

자바의 제네릭은 컴파일 시점에 타입 안정성을 검사하고 컴파일 이후에 타입 정보를 제거함 (자동 타입 캐스팅 제외)

즉, 제네릭이 제공하는 타입 안정성은 컴파일러 레벨에서만 존재하고 **타입 소거**에 의해 런타임에는 제네릭 정보가 사라짐

#### 타입 안정성 검사 및 타입 소거 동작
- 컴파일 시점
  1. 제네릭 코드에서 타입 검사
  2. 제네릭 타입을 실제 타입 또는 Object로 대체
  3. 명시적 타입 캐스팅 코드 삽입 (컴파일러의 자동 타입 캐스팅 등)
- 런타임 시점
  1. 컴파일된 바이트 코드에는 제네릭 타입 정보가 없음
  2. 모든 제네릭 타입은 Object 또는 타입 바운드(`T extends Number`)로 처리
  3. 타입 캐스팅 수행

```java
// 제네릭 사용 코드
public class TypeErasure {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Hello");
        String first = list.get(0); // 타입 캐스팅 불필요(컴파일러의 자동 타입 캐스팅 지원)
        System.out.println(fisrt);
    }
}
```

```java
// 컴파일 후 바이트로 변환된 코드(타입 소거 결과)
public class TypeErasure {
    public static void main(String[] args) {
        List list = new ArrayList(); // 제네릭 정보 제거
        list.add("Hello");
        String value = (String) list.get(0); // 컴파일러의 타입 캐스팅 코드 삽입
        System.out.println(value);
    }
}
```

#### 타입 소거를 하는 이유

1. 제네릭 도입 이전 코드와의 호환성 유지를 위해 런타임에 타입 정보 제거
2. 추가적인 타입 정보가 없으므로 바이트코드 크기를 줄일 수 있음
3. 타입 정보가 런타임에 없으면, 동일한 바이트코드로 다양한 타입의 객체 처리 가능(유연한 동작 지원)

## Type Bounds: Covariance, Contravariance, Invariance

공변(Covariance), 반공변(Contravariance), 불공변(Invariance)은 상속 관계와 타입 대체 가능성에 대한 개념으로 자바 제네릭에도 적용됨

주로 제네릭 타입 매개변수가 상위 타입(Subtyping) 관계를 어떻게 유지하는지에 따라 정의됨

#### 공변(Covariance)

공변은 같은 방향으로 유지되는 상속 관계(자식에서 부모로)를 말함

A가 B의 하위 타입이면(A가 B를 상속, 확장한 경우) `Foo<A>`는 `Foo<B>`를 허용함

자바에서의 공변은 제네릭 타입에 **Upper Bound 와일드카드**를 사용해 구현됨

Upper Bound 와일드카드
- `? extends ReferenceType`
- ReferenceType에 타입 매개변수를 지정하면(최상위 타입), 그 하위 타입까지 참조할 수 있음
- ```java
  // <? extends Number>를 Upper Bound로 지정하면, 자식 타입인 Interger, Long, Double 등의 타입 참조 가능
  List<? extends Number> list = new ArrayList<Integer>();
  List<? extends Number> list = new ArrayList<Long>();
  List<? extends Number> list = new ArrayList<Double>();
  ```

자바 공변의 특징
- **읽기는 되지만, 새 데이터를 추가할 수 없음**
- 타입 안정성을 유지하기 위해 추가를 제한함(여러 자식 타입들이 추가될 수 있기 때문에)
- ```java
  public class Variance_UpperBoundWildcard {
    public static void main(String[] args) {
  
        // 일반적인 타입 매개변수 사용
        List<Integer> normalGenericsList = new ArrayList<>();
        normalGenericsList.add(0);
        normalGenericsList.add(0);

        // upper bound: 데이터 접근 가능(consumer)
        List<? extends Number> upperBoundList = normalGenericsList;
        Number n = upperBoundList.get(0);
        System.out.println(n); 

        // upper bound: 데이터 추가 불가능(producer), 컴파일 오류
        upperBoundList.add(1L);
        upperBoundList.add(1.1);
        upperBoundList.add(1);
    }
  }
  ```

#### 반공변(Contravariance)

반공변은 반대 방향으로 유지되는 상속 관계(부모에서 자식으로)를 말함

A가 B의 하위 타입이면(A가 B를 상속, 확장한 경우) `Foo<B>`는 `Foo<A>`을 허용함

자바에서의 공변은 제네릭 타입에 **Lower Bound 와일드카드**를 사용해 구현됨

Lower Bound 와일드카드
- `? super ReferenceType`
- ReferenceType에 타입 매개변수를 지정하면(최하위 타입), 그 상위 타입까지 참조할 수 있음
- **새 데이터를 추가할 수 있지만, 읽을 수 없음**
- ```java
  public class Contravariance_LowerBoundWildcard {
    public static void main(String[] args) {

        // lower bound 지정
        List<? super Integer> lowerBoundList = new ArrayList<Number>();

        // lower bound: 데이터 추가 가능(producer)
        lowerBoundList.add(0);
        lowerBoundList.add(0);
        lowerBoundList.add(0);

        // lower bound: 데이터 접근 불가능(consumer), 컴파일 오류
        Number n = lowerBoundList.get(0);
        Integer i = lowerBoundList.get(0); 
  
        // Object로 읽기 가능 
        Object obj = lowerBoundList.get(0);
    }
  }
  ```

#### 불공변(Invariance)

불공변은 상속 관계가 유지되지 않는 관계를 말함

A가 B의 하위 타입이어도 `Foo<A>`는 `Foo<B>`는 서로 아무런 상속 관계를 가지지 않음

**자바의 제네릭은 기본적으로 불공변임**
- 서로 다른 타입 매개변수는 상속 관계와 상관없이 다른 것으로 판단함
- ```java
   public class Invariance {

    public static void main(String[] args) {
        // 자바의 제네릭은 기본적으로 불공변임
        // 따라서 List<Number>는 List<Integer>, List<Long> 등과 아무 관계가 없음
        List<Number> list1 = new ArrayList<Integer>();
        List<Number> list2 = new ArrayList<Long>();
        List<Number> list3 = new ArrayList<Double>();
        
        // 타입이 완전히 동일해야만 추가(producer), 접근(consumer) 가능
        List<Integer> integerList = new ArrayList<Integer>();
        integerList.add(1);
        Integer integer = integerList.get(0);
        System.out.println(integer);
    }
  }
  ```

#### 공변, 반공변, 불공변 비교

|     | java 키워드                  | 읽기| 쓰기| 기타                                                 |
|-----|---------------------------|--------------|--------------|----------------------------------------------------|
| 공변  | `? extends ReferenceType` | 가능           | 불가능          | 데이터 소비(Consumer): 상위 타입으로 읽기 가능, 추가 불가  - 타입 안전한 읽기 |
| 반공변 | `? super ReferenceType`   | 제한적(Object)  | 가능           | 데이터 생산(Producer): 하위 타입까지 추가 가능, 읽기 제한 - 타입 안전한 쓰기 |
| 불공변 | `ReferenceType`            | 가능           | 가능           | 타입 고정(정확히 동일한 타입만 사용), 상속 관계 고려 X - 타입 고정          |


## Type Inference

제네릭 타입 추론은 자바 컴파일러가 코드 문맥(context)과 사용된 메서드 호출 등을 기반으로 타입 매개변수를 자동으로 결정하는 기능임

명시적으로 타입 매개변수를 지정하지 않아도 컴파일러가 적절한 타입을 유추하기 때문에 간결하고 가독성 높은 코드를 작성할 수 있음

#### 타입 추론 동작 방식

메서드 호출 시 전달된 인자 값, 반환 타입, 변수 선언 등을 기반으로 제네릭 타입 매개변수 결정

또한 다이아몬드 연산자와 타겟 타입을 이용하여 타입 추론을 수행함

다이아몬드 연산자(Diamond Operator)
- 

## Parameterized Types

## Type Variables

## Generic Method

## `java.lang.reflect.Type`