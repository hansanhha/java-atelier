[Map](#map)

[Entry](#entry)

[SequencedMap](#sequencedmap)

[SortedMap](#sortedmap)

[NavigableMap](#navigablemap)

## Map

자바 컬렉션 프레임워크에 속하는 최상위 인터페이스로, Map은 해시 테이블과 같은 키와 값이 매핑된 데이터를 저장하는 자료구조임

**특징**

중복된 키를 포함하지 않음

각각의 키들은 하나의 값에 매핑됨

세 가지 컬렉션 뷰를 제공함
- `keySet`
- `values`
- `entrySet`

Map이 보관하고 있는 데이터의 순서는 컬렉션 뷰의 이터레이터가 반환하는 순서에 따라 결정됨

다만 TreeMap이나 SequencedMap 인터페이스 구현체의 경우엔 데이터의 순서가 보장됨

## Entry

맵에 저장된 키-값 쌍을 표현하는 역할을 함

맵의 요소 하나를 나타내는 Entry 인터페이스를 통해 키와 값을 개별적으로 다룰 수 있음

일반적으로 맵의 요소를 순회할 때 사용되며, 맵이 제공하는 뷰 중 entrySet() 메서드가 Entry를 반환함

## SequencedMap

자바 21에서 도입된 인터페이스로, Map 인터페이스를 상속받아 맵의 기본 기능을 모두 포함하며, 순차적 순서를 다루는 추가 메서드를 정의함

입력 순서 또는 순차 순서를 보장하며, 입력된 순서와 반대 순서(reverse order)로 요소에 접근할 수 있는 기능을 제공함

**순서 보장**

맵에 추가된 요소의 순서를 보장함

기본적으로 요소가 추가된 순서를 유지하거나, 제거된 순서에 대한 추적을 할 수 있음

**순차 접근 메서드**

Map의 컬렉션 뷰(keySet, values, entrySet)처럼 순차적으로 요소에 접근할 수 있는 default 메서드 제공
- `sequencedKeySet`
- `sequencedValues`
- `sequencedEntrySet`

## SortedMap

SequencedMap을 상속받는 맵으로, 키를 정렬된 순서로 유지하는 특성을 가짐 

키의 Comparable에 의한 자연 순서(natural ordering) 또는 Comparator에 따라 자동으로 정렬되도록 보장함

**범위 뷰 메서드**

특정 범위의 키-값 쌍을 뷰로 제공하는 메서드를 통해 맵의 일부분을 쉽게 접근할 수 있음

`subMap(K, fromKey, K to Key)` `headMap(K toKey)`, `tailMap(K fromKey)`

**최소/최대 키 접근 메서드**

가장 작은 키와 가장 큰 키에 접근할 수 있는 메서드를 제공함

`firstKey()`, `lastKey()`

**특징**

자동 정렬 지원, 탐색 기능(최댓값, 최솟값, 범위 검색 등)

특정 순서로 정렬된 데이터가 필요하거나, 범위 검색이 중요한 경우에 유용함 

## NavigableMap

