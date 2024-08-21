[HashMap](#hashmap)

[트리화](#트리화)

[코드 분석](#코드-분석)

## HashMap

자바의 해시맵은 키-값 쌍(엔트리)을 저장하고 검색할 수 있는 해시 테이블 기반의 컬렉션임

`Map<K, V>` 인터페이스를 구현하고 `AbstractMap<K,V>` 로부터 상속받고 있음

### 특징

#### 키와 값의 매핑

키를 기준으로 값을 저장하는 구조임

각 키는 해시 함수를 통해 해시 코드로 변환되고, 이 값을 기반으로 데이터를 저장할 위치를 결정함

#### 중복 허용

키는 중복을 허용하지 않음. 고유해야 됨

동일한 키로 다시 값을 추가하면 기존 값이 덮어씌워짐

값은 중복을 허용함

#### 순서 보장

해시맵은 내부적으로 데이터의 순서를 유지하지 않음

따라서 데이터를 삽입한 순서와 검색할 때의 순서가 다를 수 있음

순서 보장이 필요한 경우 `LinkedHashMap`을 사용할 수 있음

#### null 허용

키와 값 모두에 대해 허용됨

단 키는 고유값을 가져야 하므로, null 키 역시 단 하나만 허용됨

#### 비동기적

동기적으로 동작하지 않기 때문에 멀티 스레드 환경에서 취약함

여러 스레드에서 접근할 수 있도록 안전하게 사용하려면 Collections.synchronizedMap 또는 ConcurrentMap 같은 구현체를 사용해야 됨

#### vs HashTable

자바엔 해시맵과 더불어서 해시 테이블 구현체도 존재하는데, 키와 값에 null을 허용하고 동기화를 지원하지 않는 해시맵과 달리

자바의 해시 테이블은 키와 값에 null 허용하지 않고 synchronized 키워드를 사용해서 동기화를 지원함

## 트리화

HashMap은 기본적으로 버킷에 해시 충돌을 처리하기 위한 단일 연결 리스트를 사용하고 있음

해시 충돌이 발생한 경우 동일한 해시 코드를 가진 여러 키-값 쌍을 저장함

만약 버킷(리스트)이 너무 커질 경우 해시맵은 연결 리스트를 트리 구조로 변환함

TreeMap과 유사한 구조로, 각 노드가 TreeNode로 변환됨

트리화된 버킷은 이진 트리로 변환되어 최악의 경우에도 `O(log n)`의 탐색 시간을 보장함

#### 임계값

TreeNode는 일반 노드보다 약 2배 정도 크기 때문에, `TREEIFY_THRESHOLD`라는 임계값을 넘는 경우에만 트리화가 발생함

만약 버킷의 크기가 작아지면 다시 연결 리스트로 변환됨

#### 정렬 및 비교

트리화된 버킷의 노드는 주로 hashCode를 기준으로 정렬됨

동일한 해시 코드를 가진 두 키가 `Comparable` 인터페이스를 구현한 경우, compareTo 메서드를 사용해 추가적인 정렬을 수행함

## 코드 분석

### 주요 필드

#### 정적 필드

```java
// 기본 초기 용량
// 왼쪽 시프트 연산은 곱셈 연산과 동일하게 작동함
// x << n은 x를 2의 n승만큼 곱하는 것과 동일함
// 반대로 오른쪽 시프트 연산은 x를 2의 n승만큼 나누는것과 동일함
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

// 최대 용량 (약 10억정도의 값을 가질 수 있음)
static final int MAXIMUM_CAPACITY = 1 << 30;

// 기본 로드 팩터
static final float DEFAULT_LOAD_FACTOR = 0.75f;

/*
    트리화 임계값
    버킷 내에 저장된 노드의 수가 이 값을 초과할 때 연결 리스트 구조를 트리 구조로 변환하기 위한 임계값임
    충돌이 많이 발생하면 연결 리스트의 길이가 길어져 성능이 저하될 수 있는데, 
    이를 방지하고자 버킷 내의 노드가 이 값을 초과하면 해당 연결 리스트는 이진 검색 트리로 변환함         
 */
static final int TREEIFY_THRESHOLD = 8;

/*
    트리 구조로 변환된 버킷을 다시 연결 리스트로 변환되는 임계값
    트리 구조의 버킷에 요소가 제거되어 노드의 수가 이 값 미만으로 감소하면 트리는 다시 연결 리스트로 변환됨
    연결 리스트 구조보다 트리 구조가 메모리를 더 사용하기 때문에 불필요한 메모리 사용을 줄이기 위함
 */
static final int UNTREEIFY_THRESHOLD = 6;

/*
    트리화가 되기 위한 최소 용량
    해시맵이 충분히 큰 경우에만 버킷 구조를 트리로 변환할 수 있음
    이 값만큼 용량이 크지 않으면 트리화 임계값을 넘는다고 해도 트리로 변환되지 않음
 */
static final int MIN_TREEIFY_CAPACITY = 64;
```

#### 일반 필드

```java
/*
    실제 데이터인 키-값 쌍을 담고 있는 엔트리 배열 (각 인덱스에는 연결 리스트 또는 트리 형태로 노드들이 저장됨)
    처음 사용할 때 초기화되고 필요에 따라 크기를 조정함
    새로 할당되는 길이는 두 배씩 증가함
 */
transient Node<K, V>[] tables;

// entrySet() 캐시
transient Set<Map.Entry<K, V>> entrySet;

/*
    현재 맵이 가지고 있는 키-값 쌍의 개수
    해시 맵의 크기를 결정하는 역할을 하며, 용량 초과 여부를 판단하는 데 사용됨
 */
transient int size;

// 이터레이터 fail-fast용 필드
transient int modCount;

/*
    리사이즈 되기 전 최대 키-값 쌍의 수를 나타냄
    capacity * loadFactory로 계산됨
    해시맵의 사이즈가 이 값을 초과하면 table 배열의 크기를 두 배로 늘리고, 모든 항목을 새로운 배열에 재배치함(리사이징)
    해시 테이블에서 리사이징 과정은 모든 항목에 대해 해시 값을 재계산해야 되므로 오버헤드를 발생시킴
 */
int threshold;

/*
    해시 테이블 특성 상 엔트리의 개수가 많아지면 해시 충돌이 빈번하게 발생하기 때문에
    일정 범위만큼 채워지면 해시 테이블 크기를 늘려야되는데, 로드 팩터가 그 기준점을 잡아줌
    즉, table 배열에 키-값 쌍이 얼마만큼의 일정 비율로 채워졌을 때 리사이즈를 일으킬지 결정함
    loadFactory의 값이 0.75라면 배열의 75%가 채워졌을 때 리사이징이 일어남 
 */
final float loadFactor;
```

### 생성자

```java
// 초기 용량과 loadFactor를 매개변수로 받는 생성자
public HashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                loadFactor);
    this.loadFactor = loadFactor;
    this.threshold = tableSizeFor(initialCapacity);
}
```


