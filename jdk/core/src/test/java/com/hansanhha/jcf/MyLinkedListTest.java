package com.hansanhha.jcf;

import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("MyLinkedList 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MyLinkedListTest {

    List<Integer> list;

    @BeforeEach
    void init() {
        list = new MyLinkedList<>();
    }

    @Test
    void 요소_삽입() {
        // given
        int val = 1;

        // when
        list.add(val);

        // then
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    void 인덱스_요소_삽입() {
        // given
        list.add(1);
        list.add(2);
        list.add(4);
        list.add(5);

        // when
        list.add(2, 3);

        // then
        assertThat(list.size()).isEqualTo(5);
    }

    @Test
    void 컬렉션_삽입() {
        // given
        var collection = List.of(1,2,3,4,5);

        // when
        list.addAll(collection);

        // then
        assertThat(list.size()).isEqualTo(collection.size());
    }

    @Test
    void 인덱스_컬렉션_삽입() {
        // given
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(9);
        list.add(10);

        var collection = List.of(4,5,6,7,8);

        // when
        list.addAll(collection);

        // then
        assertThat(list.size()).isEqualTo(10);
    }

    @Test
    void 맨_처음_삽입_addFirst() {
        // given
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        // when
        list.addFirst(1);

        // then
        assertThat(list.size()).isEqualTo(5);
    }

    @Test
    void 맨_마지막_삽입_addLast() {
        // given
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        // when
        list.addLast(5);

        // then
        assertThat(list.size()).isEqualTo(5);

    }

}