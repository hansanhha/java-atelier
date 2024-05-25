## Sealed Classes, interfaces

다른 클래스와 인터페이스의 상속/구현을 제한할 수 있음

seal : 봉인하다


## Sealed Classes 선언


```java
public sealed class Shape permits Circle, Square, Rectangle {
    
} 
```

sealed 제어자 선언

permits 절에 extend를 허용할 클래스 명시

Circle, Square, Rectangle 클래스는 sealed class와 동일한 모듈이나 패키지에 위치

```
public final class Circle extends Shape {
    public float radius;
}
```

허용된 자식은 상속할 수 있음

```java
public non-sealed class Square extends Shape {
    public double side; 
}
```

non-sealed 클래스는 허용된 자식 클래스 제약 확인

```java
public sealed class Rectangle extends Shape permits FilledRectangle {
    public double length; width;
}
```

sealed class를 상속받은 클래스도 sealed class로 만들 수 있음

```java
package com.example.geometry;

public sealed class Figure{ }

final class Circle extends Figure {
    float radius;
}
non-sealed class Square extends Figure {
    float side;
}
sealed class Rectangle extends Figure {
    float length, width;
}
final class FilledRectangle extends Rectangle {
    int red, green, blue;
}
```

아예 같은 파일 안에 sealed class와 상속을 허용할 클래스를 적는 방법도 있음

## 허용된 자식 클래스 제약

- 컴파일 시점에 sealed class에서 허용된 자식 클래스로 접근할 수 있어야 됨
    - 자식 클래스 또한 sealed class라면 허용된 그 자식 클래스에도 접근할 수 있어야 됨
- 허용된 자식 클래스는 직접 sealed class를 extend 해야 됨
- 허용된 자식 클래스는 sealing 관리를 위해 3가지 제어자 중 하나를 선택해야 됨
    - final  : 더 이상 상속을 하지 못하게 함 
    - sealed : 허용된 클래스만 상속 가능
    - non-sealed : 누구나 상속 가능 - 부모 sealed class에서 막지 못함지
- 허용된 자식 클래스는 sealed class와 같은 모듈이나 동일한 패키에 위치해야 함

