## SynchronizedList

`Collections.synchronizedList`

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