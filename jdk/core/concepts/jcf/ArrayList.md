[ArrayList](#arraylist)

[동시성 이슈 해결방안](#동시성-이슈-해결방안)

[ArrayList 분석](#arraylist-분석)

[ArrayList 구현](#arraylist-구현)

## ArrayList

배열의 크기를 조정할 수 있는 List 인터페이스 구현체
- List 인터페이스의 모든 optional 작업 구현
- element 타입으로 모든 타입을 받음 (null 포함)
- 요소를 삽입하기 전에 내부적으로 공간이 부족한 지 확인 후 삽입 (부족하다면 길이 조정)

**주의사항**

1. ArrayList는 **동기화되지 않음**

여러 스레드에서 동일한 ArrayList에 접근해서 삽입/삭제/크기 조정한다면 데이터 손실이나 예외가 발생함

(요소의 값을 바꾸는 건 해당되지 않음)

따라서 ArrayList 외부에서 동기화를 보장하거나 thread-safe 리스트 구현체를 사용해야 됨

2. ArrayList의 `iterator`, `listIterator` 메서드에서 반환한 iterator는 빠른 실패(fail-fast)를 일으킬 수 있음

iterator가 생성된 후로 다른 스레드에서 ArrayList를 삽입/삭제 등의 작업을 수행하면 (다만 iterator 자체적으로 삽입/삭제 등의 작업은 예외)

추후에 애매한 상황을 만들지 않고자 바로 `ConcurrentModificationException`을 터뜨림

## 동시성 이슈 해결방안

[`Collections.synchronizedList`](./SynchronizedList.md)

[CopyOnWriteArrayList](./CopyOnWriteArrayList.md)

## ArrayList 분석

### 계층 구조

<img src="./images/arraylist hierarchy.png" alt="ArrayList 계층 구조" style="width: 70%; height: 70%"/>

### 주요 필드

```java
// 기본 용량
private static final int DEFAULT_CAPACITY = 10;

// ArrayList 요소가 저장되는 필드
Object[] elementData;

// 현재 저장된 요소의 개수
private int size;
```

### 메서드

목록
- add, addAll, addFist, addLast
- remove, removeAll, removeFist, removeLast, removeIf
- retainAll, replaceAll
- set, get, getFist, getLast, subList, indexOf
- grow, trimToSize, ensureCapacity

### 생성자

기본 생성자, capacity를 받는 생성자는 간단해서 제외

Collection 타입을 매개변수로 받는 생성자(Collection -> ArrayList)

```java
public ArrayList(Collection<? extends E> c) {
        Object[] a = c.toArray();
        if ((size = a.length) != 0) {
            if (c.getClass() == ArrayList.class) {
                elementData = a;
            } else {
                elementData = Arrays.copyOf(a, size, Object[].class);
            }
        } else {
            // replace with empty array.
            elementData = EMPTY_ELEMENTDATA;
        }
    }
```

위의 코드에서 주의깊게 볼 부분

1. `c.getClass() == ArrayList.class`

   `c.getClass().isAssignableFrom(ArrayList.class)` 대신 직접 비교를 하는 코드를 사용 

    isAssignableFrom()은 상속 관계를 검사하는데 사용함

    상대적으로 느린 메서드를 사용하지 않고 정확한 타입을 비교하기 위해 `==` 연산자 사용

2. `elementData = Arrays.copyOf(a, size, Object[].class)`

   위에서 `Object[] a = c.toArray()`로 매개변수로 받은 Collection 구현체를 배열로 변환한 후

   해당 배열을 복사한 새로운 Object[] 배열을 ArrayList의 필드에 할당함

   Collection 구현체의 배열을 그대로 사용할 경우 외부에서 수정될 위험이 있으므로 데이터 무결성을 보장하기 위함임

### grow (동적 배열 크기 조정)

자동으로 ArrayList가 가진 배열의 크기를 스스로 조정하는 메서드

ArrayList의 가장 핵심이지 않나 싶음

```java
private Object[] grow(int minCapacity) {
        int oldCapacity = elementData.length;
        if (oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            int newCapacity = ArraysSupport.newLength(oldCapacity,
                    minCapacity - oldCapacity, /* minimum growth */
                    oldCapacity >> 1           /* preferred growth */);
            return elementData = Arrays.copyOf(elementData, newCapacity);
        } else {
            return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }
    }
```

현재 배열의 크기를 oldCapacity에 할당 후, 분기 처리

- 요소를 가지고 있는 경우(oldCapacity > 0)
  - ArraysSupport.newLength()를 통해 새로운 배열의 크기를 정하고, Arrays.copyOf()를 통해 기존의 배열보다 길이가 길어진 값이 복사된 새 배열을 할당함
- 없는 경우
  - grow() 매개변수로 받은 값과 DEFAULT_CAPACITY 중 큰 값을 크기로 갖는 새 배열 할당

ArraysSupport는 jdk.internal.util 패키지에 속한 클래스로 jdk 내부적으로만 사용할 수 있는 클래스임

```java
// package jdk.internal.util;
// ArraysSupport.newLength()

public static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

public static int newLength(int oldLength, int minGrowth, int prefGrowth) {
        int prefLength = oldLength + Math.max(minGrowth, prefGrowth); // might overflow
        if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
            return prefLength;
        } else {
            // put code cold in a separate method
            return hugeLength(oldLength, minGrowth);
        }
    }

private static int hugeLength(int oldLength, int minGrowth) {
    int minLength = oldLength + minGrowth;
    if (minLength < 0) { // overflow
        throw new OutOfMemoryError(
                "Required array length " + oldLength + " + " + minGrowth + " is too large");
    } else if (minLength <= SOFT_MAX_ARRAY_LENGTH) {
        return SOFT_MAX_ARRAY_LENGTH;
    } else {
        return minLength;
    }
}

// oldLength: 현재 배열의 크기
// minGrowth: 최소 증가량
// prefGrowth: 선호 증가량
```

- 최소 증가 길이와 선호 증가 길이 중 큰 값과 현재 배열의 크기를 더한 값이 기준에 적합하다면 이 값을 리턴함
- 그렇지 않은 경우 hugeLength()를 통해 현재 배열 크기와 최소 증가 길이를 더한 값이 `SOFT_MAX_ARRAY_LENGTH`보다 작거나 동일하다면 `SOFT_MAX_ARRAY_LENGTH`(Integer.MAX_VALUE - 8)의 값을 리턴함

```java
private Object[] grow() {
        return grow(size + 1);
}
```

add()에서 grow()를 호출하는데, grow()는 다시 grow(size + 1)을 호출함

즉, grow(int minCapacity)의 매개변수 값은 현재 배열이 가진 요소의 개수에 1을 더한 값임

따라서 newLength()에 전달되는 minGrowth의 값은 (전체 요소 개수 + 1) - 배열의 길이(메모리에 할당된 공간의 크기)가 되고

prefGrowth의 경우 `oldCapacity >> 1` 비트 연산자를 사용해서 오른쪽으로 1비트씩 이동(shift)하고 있는데 이건 2로 나눈 값과 동일한 값으로, 배열의 절반 길이의 값임

```java
private void add(E e, Object[] elementData, int s) {
    if (s == elementData.length)
        elementData = grow();
    elementData[s] = e;
    size = s + 1;
}
```

add 메서드에서 값을 넣기 전에 전체 요소의 개수가 배열의 길이와 동일한 경우 grow()를 호출하는데,

grow()에서 size+1을 값으로 전달하기에 newLength에 전달되는 minGrowth의 값은 항상 1이 되므로, 배열 길이의 절반 값을 가진 prefGrowth가 항상 큰 걸 알 수 있음

고로 **ArrayList는 내부적으로 배열 길이를 늘릴 때, 자신의 배열 길이의 절반을 늘린다는 것**을 알 수 있음

## [ArrayList 구현](../../src/main/java/com/hansanhha/jcf/MyArrayList.java)