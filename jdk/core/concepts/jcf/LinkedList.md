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
- linkFirst(E)
- linkLast(E)
- linkBefore(E, Node<E>)
- unLinkFirst(Node<E>)
- unLinkLast(Node<E>)
- unLink(Node<E>)

### 삽입

*add(E): 요소 삽입**

**add(int, E): 특정 인덱스에 요소 삽입**

**addAll(Collection<? extends E>): 컬렉션 삽입**

**addAll(int, Collection<? extends E>): 특정 인덱스에 요소 삽입**

**addFirst(E): 맨 앞에 삽입**

**addLast(E): 맨 뒤에 삽입**

### 삭제

### 조회

### 값 수정

### of

### toArray

## LinkedList 구현

## 테스트 코드