[LinkedList 특징](#linkedlist-특징-및-상속-관계)

[LinkedList 분석](#linkedlist-분석)

- [필드, 내부 클래스](#필드-내부-클래스)
- [내부 메서드](#내부-메서드)
- [삽입](#삽입)
- [삭제](#삭제)
- [조회](#조회)
- [값 수정](#값-수정)
- [of](#of)
- [toArray](#toarray)

[LinkedList 구현](#linkedlist-구현)

[테스트 코드](#테스트-코드)

## LinkedList 특징 및 상속 관계

<img src="./images/LinkedList Hierarchy.png" alt="LinkedList Hierarchy" style="width: 60%; height: 60%;" >

**상속 관계**

- List와 Deque 인터페이스 구현
- AbstractSequentialList 상속

**특징**

- 이중 연결 리스트
- 모든 타입 허용 (null 포함)
- 순회가 필요한 경우

**동기화 불가**

- LinkedList는 동기화를 지원하지 않음
- 따라서 여러 스레드에서 동시에 접근했을 때 어느 한 스레드에서 구조적 수정을 하는 경우 동기화가 필요함
- [Collections.synchronizedList()](./SynchronizedList.md)를 통해 동기화 처리 가능

**이터레이터**

- 이터레이터 생성 후 구조적 수정이 이뤄지면 fail-fast 발생 (`ConcurrentModificationException`)

## LinkedList 분석

### 필드, 내부 클래스

**필드**

- `int size`: 현재 요소의 개수
- `Node<E> first`: head 노드
- `Node<E> last`: tail 노드

**내부 클래스**

- 연결 리스트의 노드를 나타내는 노드 클래스

자바의 LinkedList는 이중 연결 리스트로, Node 클래스는 prev, next 필드를 가지고 있음

```java
private static class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
```

이터레이터

- DescendingIterator
- ListItr
- LLSpliterator
- RandomAccessSpliterator

뷰

- ReverseOrderLinkedListView

### 내부 메서드

연결 리스트 요소들의 참조 작업을 내부 메서드를 통해 공통적으로 수행함

#### linkFirst(E)

```java
// 연결 리스트의 head 노드에 새 노드를 삽입하는 메서드
private void linkFirst(E e) {
    final Node<E> f = first;
    final Node<E> newNode = new Node<>(null, e, f);
    first = newNode;
    // 만약 기존 head 노드가 null이라면 연결 리스트에 아무 노드가 없다는 뜻이므로, 삽입하려는 메서드가 head 노드이자 tail 노드가 됨
    if (f == null)
        last = newNode;
    // 그렇지 않다면 기존 head의 prev를 새 노드로 연결시켜줌
    else
        f.prev = newNode;
    size++;
    modCount++;
}
```

#### linkLast(E)

```java
// linkFirst와는 반대로, tail 노드에 새 노드를 삽입하는 메서드
void linkLast(E e) {
    final Node<E> l = last;
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    // 만약 기존 tail 노드가 null이라면 연결 리스트에 아무 노드가 없다는 뜻이므로, 삽입하려는 메서드가 head 노드이자 tail 노드가 됨
    if (l == null)
        first = newNode;
    // 그렇지 않다면 기존 tail 노드의 next를 새 노드로 연결시켜줌
    else
        l.next = newNode;
    size++;
    modCount++;
}
```

#### linkBefore(E, Node<E>)

```java
// linkBefore 메서드는 linkFirst와 linkLast 메서드와 달리, 특정 지점에 노드를 삽입하는 메서드로, 매개변수로 받은 succ 노드의 이전(prev)에 새 노드를 삽입함

// succ: successor의 줄임말로 어떤 노드의 다음 노드(next)를 말함
// pred: predecessor의 줄임말로 어떤 노드의 이전 노드(prev)를 말함
void linkBefore(E e, Node<E> succ) {
    // assert succ != null;
    final Node<E> pred = succ.prev;
    final Node<E> newNode = new Node<>(pred, e, succ);
    // succ 노드의 이전 노드를 새 노드로 지정함
    succ.prev = newNode;
    // 만약 pred(`succ.prev`)가 null인 경우 succ 노드가 head 노드라는 것을 의미하므로 새 노드를 head 노드로 지정함
    if (pred == null)
        first = newNode;
    // 아닌 경우 원래 succ 노드의 이전 노드의 next를 새 노드를 가리키게 함
    else
        pred.next = newNode;
    size++;
    modCount++;
}
```

#### unLinkFirst(Node<E>)

```java
// head 노드를 삭제하는 함수
private E unlinkFirst(Node<E> f) {
    // assert f == first && f != null;
    final E element = f.item;
    final Node<E> next = f.next;
    // 기존 head 노드의 값을 null 처리(GC)
    f.item = null;
    f.next = null; // help GC
    // head 노드를 기존 head의 다음 노드로 지정하고, 
    first = next;
    // 만약 다음 노드가 null이라면 기존 head 노드를 제외하고 연결 리스트에 아무 노드가 없다는 것이므로 tail 노드도 null 처리
    if (next == null)
        last = null;
    // 아닌 경우 다음 노드의 prev 값을 null 처리
    else
        next.prev = null;
    size--;
    modCount++;
    return element;
}
```

#### unLinkLast(Node<E>)

```java
// tail 노드를 삭제하는 함수
private E unlinkLast(Node<E> l) {
    // assert l == last && l != null;
    final E element = l.item;
    final Node<E> prev = l.prev;
    // 기존 tail 노드의 값을 null 처리(GC)
    l.item = null;
    l.prev = null; // help GC
    // tail 노드를 기존 tail 노드의 이전 노드로 지정하고, 
    last = prev;
    // 만약 이전 노드가 null이라면 기존 tail 노드를 제외하고 연결 리스트에 아무 노드가 없다는 것이므로 head 노드도 null 처리
    if (prev == null)
        first = null;
    // 아닌 경우 다음 노드의 next 값을 null 처리
    else
        prev.next = null;
    size--;
    modCount++;
    return element;
}
```

#### unLink(Node<E>)

```java
// 매개변수로 받은 노드를 삭제하는 함수
E unlink(Node<E> x) {
    // assert x != null;
    final E element = x.item;
    final Node<E> next = x.next;
    final Node<E> prev = x.prev;

    // 먼저 prev 값을 기반으로 다음 노드 연결 처리
    // prev 값이 null인 경우 x 노드는 head 노드를 의미하므로, x의 다음 노드를 head로 지정
    if (prev == null) {
        first = next;
    }
    // 아닌 경우 x 노드의 이전 노드와 x 노드의 다음 노드를 연결
    else {
        prev.next = next;
        x.prev = null;
    }

    // 그 다음 next 값을 기반으로 이전 노드 연결 처리
    // next 값이 null인 경우 x 노드는 tail 노드를 의미하므로, x의 이전 노드를 tail로 지정
    if (next == null) {
        last = prev;
    } 
    // 아닌 경우 x 노드의 다음 노드와 x 노드의 이전 노드를 연결
    else {
        next.prev = prev;
        x.next = null;
    }

    x.item = null;
    size--;
    modCount++;
    return element;
}
```

#### node(int)

```java
// 특정 인덱스에 위치한 노드를 반환하는 함수
Node<E> node(int index) {
    // assert isElementIndex(index);

    // 인덱스의 값이 리스트의 절반보다 작은 경우 head 노드에서 순회
    if (index < (size >> 1)) {
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } 
    // 큰 경우 tail 노드에서 순회
    else {
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}
```

### 삽입

#### add(E): 요소 삽입

```java
public boolean add(E e) {
    linkLast(e);
    return true;
}
```

[`linkLast(E)`](#linklaste) 메서드를 사용해서 삽입함

#### add(int, E): 특정 인덱스에 노드 삽입

```java
public void add(int index, E element) {
    checkPositionIndex(index);

    // 특정 인덱스에 노드를 삽입하는 메서드로 index의 크기에 따라 삽입 위치를 정함
    // index가 size와 같은 경우 맨 뒤에 삽입
    if (index == size)
        linkLast(element);
    // 아닌 경우 linkBefore(Node<E>) 메서드를 사용해서 index에 위치한 기존 노드 이전에 삽입
    else
        linkBefore(element, node(index));
}
```

#### addAll(Collection<? extends E>): 노드 컬렉션 삽입

```java
public boolean addAll(Collection<? extends E> c) {
    return addAll(size, c);
}
```

[addAll(int, Collection<? extends E>)](#addallint-collection-extends-e-특정-인덱스에-노드-컬렉션-삽입) 메서드 위임

#### addAll(int, Collection<? extends E>): 특정 인덱스에 노드 컬렉션 삽입

```java
public boolean addAll(int index, Collection<? extends E> c) {
    checkPositionIndex(index);

    // 매개변수로 받은 컬렉션을 배열로 변환 후 길이 체크
    Object[] a = c.toArray();
    int numNew = a.length;
    if (numNew == 0)
        return false;

    // 매개변수로 받은 index 값에 따라 pred, succ 노드의 값 지정
    // succ: successor의 줄임말로 어떤 노드의 다음 노드(next)를 말함
    // pred: predecessor의 줄임말로 어떤 노드의 이전 노드(prev)를 말함
    Node<E> pred, succ;
    // index가 size와 같은 경우, 맨 뒤에 컬렉션을 삽입하겠다는 의미
    // succ 노드는 null 
    // pred 노드는 tail 노드로 지정
    if (index == size) {
        succ = null;
        pred = last;
    }
    // 아닌 경우 리스트 중간에 컬렉션을 삽입하겠다는 의미
    // node(int) 메서드를 통해 index에 위치한 노드를 succ 노드로, succ 노드의 이전 노드를 pred 노드로 지정
    else {
        succ = node(index);
        pred = succ.prev;
    }

    // 배열로 변환한 컬렉션 요소들을 새 노드로 삽입
    for (Object o : a) {
        @SuppressWarnings("unchecked") E e = (E) o;
        // prev 값에 pred 지정, next 값은 null로 지정
        Node<E> newNode = new Node<>(pred, e, null);
        // pred가 null인 경우, 연결 리스트에 노드가 하나도 없는 경우를 말함
        // head 노드에 새 노드를 지정
        if (pred == null)
            first = newNode;
        // 아닌 경우 pred의 next와 새 노드를 연결
        else
            pred.next = newNode;
        // 새 노드를 pred로 지정하여, 다음 생성될 노드가 새 노드와 연결되도록 함
        pred = newNode;
    }

    // 위의 루프문을 돌면 삽입되기 전 연결 리스트의 pred 노드와 컬렉션 요소로 만들어진 새 노드들이 모두 연결된 상태임
    // 새로 삽입된 맨 마지막 노드의 next 값은 지정되지 않았기 때문에 이 부분만 처리해주면 됨
    // succ의 값이 null인 경우 컬렉션이 리스트의 끝부터 삽입된 것을 의미하므로 새로 삽입된 맨 마지막 노드가 tail 노드가 됨 
    if (succ == null) {
        last = pred;
    }
    // 아니라면 컬렉션이 중간에 삽입된 것을 의미하므로, 새로 삽입된 맨 마지막 노드와 기존 노드를 연결해줌
    else {
        pred.next = succ;
        succ.prev = pred;
    }

    size += numNew;
    modCount++;
    return true;
}
```

#### addFirst(E): 맨 앞에 삽입

```java
// linkFirst(E) 위임
public void addFirst(E e) {
    linkFirst(e);
}
```

#### addLast(E): 맨 뒤에 삽입

```java
// linkLast(E) 위임
public void addLast(E e) {
    linkLast(e);
}
```

### 삭제



### 조회

### 값 수정

### of

### toArray

## LinkedList 구현

## 테스트 코드