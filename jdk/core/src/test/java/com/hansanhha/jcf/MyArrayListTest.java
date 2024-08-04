package com.hansanhha.jcf;

import org.junit.jupiter.api.*;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("MyArrayList 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MyArrayListTest {

    List<Integer> list;

    @BeforeEach
    void init() {
        list = new MyArrayList<>();
    }

    @Test
    void 요소만_삽입() {
        list.add(1);
        list.add(2);
        list.add(3);

        assertThat(list.size()).isEqualTo(3);
        assertThat(list).containsSequence(1,2,3);
    }

    @Test
    void 인덱스_지정_삽입() {
        list.add(1);
        list.add(2);
        list.add(4);
        list.add(5);

        list.add(2, 3);

        assertThat(list.size()).isEqualTo(5);
        assertThat(list).containsSequence(1,2,3,4,5);
    }

    @Test
    void 컬렉션_삽입() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        var other = new LinkedList<Integer>();
        other.add(6);
        other.add(7);
        other.add(8);
        other.add(9);
        other.add(10);

        list.addAll(other);

        assertThat(list.size()).isEqualTo(10);
        assertThat(list).containsSequence(1,2,3,4,5,6,7,8,9,10);
    }

    @Test
    void 컬렉션_인덱스_지정_삽입() {
        list.add(1);
        list.add(2);
        list.add(10);

        var other = new LinkedList<Integer>();
        other.add(3);
        other.add(4);
        other.add(5);
        other.add(6);
        other.add(7);
        other.add(8);
        other.add(9);

        list.addAll(2, other);

        assertThat(list.size()).isEqualTo(10);
        assertThat(list).containsSequence(1,2,3,4,5,6,7,8,9,10);
    }

    @Test
    void 맨_처음_삽입() {
        list.add(2);
        list.add(3);
        list.add(4);

        list.addFirst(1);

        assertThat(list.size()).isEqualTo(4);
        assertThat(list).containsSequence(1,2,3,4);
    }

    @Test
    void 맨_마지막_삽입() {
        list.add(1);
        list.add(2);
        list.add(3);

        list.addLast(4);

        assertThat(list.size()).isEqualTo(4);
        assertThat(list).containsSequence(1,2,3,4);
    }

}