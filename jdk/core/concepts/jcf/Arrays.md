[Array-Based Classes](#array-based-classes)

[Arrays](#arrays)

[ArrayList](#arraylist)

## Array-Based Classes

Java Collection Framework에 속하는 배열 관련 클래스들
- [Arrays](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html)
- [ArrayList](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html)
- [ArrayBlockingQueue](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ArrayBlockingQueue.html)
- [CopyOnWriteArrayList](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CopyOnWriteArrayList.html)
- [CopyOnWriteArraySet](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CopyOnWriteArraySet.html)

## Arrays

배열을 유용하게 조작할 수 있는 메서드들을 가진 유틸 클래스

크게 아래와 같은 유형을 지원함
- 이진 탐색(binarySearch)
- 정렬(sort, parallelSort)
- 비교(compare)
- 복사(copyOf)
- 할당(fill)
- 다른 값 찾기(mismatch)
- List 변환(toList)
- Stream 변환(stream)

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

**해결방안**

1. Collections.synchronizedList

아래의 Collections.synchronizedList의 메서드 바디를 보면 List 타입의 매개변수를 받아서

랜덤 액세스 분기 처리 후 `SynchronizedRandomAccessList` 또는 `SynchronizedList`를 반환함(둘의 동작 방식은 크게 차이가 나지 않음)

```java
// Collections.synchronizedList()
public static <T> List<T> synchronizedList(List<T> list) {
    return (list instanceof RandomAccess ?
            new SynchronizedRandomAccessList<>(list) :
            new SynchronizedList<>(list));
}
```

SynchronizedList는 메서드 단위로 동기화를 함

어떤 스레드에서 이 리스트의 메서드를 사용하고 있는 동안에는 다른 스레드에서 리스트를 수정할 수 없음

```java
// Collections.SynchronizedList class
static class SynchronizedList<E>
            extends SynchronizedCollection<E>
            implements List<E> {

        public E get(int index) {
            synchronized (mutex) {
                return list.get(index);
            }
        }

        public E set(int index, E element) {
            synchronized (mutex) {
                return list.set(index, element);
            }
        }

        public void add(int index, E element) {
            synchronized (mutex) {
                list.add(index, element);
            }
        }

        public E remove(int index) {
            synchronized (mutex) {
                return list.remove(index);
            }
        }
    }
```

```java
// synchronizedList 사용 예시
var arrayList = new ArrayList<Integer>();
var synchronizedArrayList = Collections.synchronizedList(arrayList);
```

2. CopyOnWriteArrayList

thread-safe한 ArrayList로 add, set 등의 수정 작업이 발생하면 원본 배열을 새 배열로 복사하는 방식으로 동작함

이 방식은 비용이 많이 들긴 하지만 다음의 경우에 유용함

- 순회 연산(traversal operation)이 수정 연산보다 많은 경우 (수정 연산이 많은 경우엔 성능 저하 발생)
- 순회 연산을 동기화할 수 없는 경우
- 동시 스레드 간의 간섭을 방지하고 싶은 경우

CopyOnWriteArrayList의 iterator 메서드는 "snapshot" 개념처럼 iterator 생성된 시점의 배열 상태를 참조함

iterator가 동작하는 동안에는 배열이 변경되지 않고 간섭되지 않게 함으로써 iterator가 `ConcurrentModificationException`를 던지지 않도록 보장함

또한 iterator에서 수정 작업을 지원하지 않으므로 호출하면 `UnsupportedOperationException`를 터뜨림

```java
var list = new CopyOnWriteArrayList<Integer>();

list.add(1);
list.add(2);
```




