package com.hansanhha.jcf;

import java.util.Iterator;
import java.util.SequencedCollection;
import java.util.SequencedSet;

public interface MySequencedMap<K, V> extends MyMap<K, V> {

    MySequencedMap<K, V> reversed();

    default MyMap.Entry<K, V> firstEntry() {
        var it = entrySet().iterator();
        return it.hasNext() ? new MyNullableKeyValueHolder<>(it.next()) : null;
    }

    default MyMap.Entry<K, V> lastEntry() {
        var it = reversed().entrySet().iterator();
        return it.hasNext() ? new MyNullableKeyValueHolder<>(it.next()) : null;
    }

    default MyMap.Entry<K, V> pollFirstEntry() {
        var it = entrySet().iterator();

        if (it.hasNext()) {
             var entry = new MyNullableKeyValueHolder<>(it.next());
             it.remove();
             return entry;
        }

        return null;
    }

    default MyMap.Entry<K, V> pollLastEntry() {
        var it = reversed().entrySet().iterator();

        if (it.hasNext()) {
            var entry = new MyNullableKeyValueHolder<>(it.next());
            it.remove();
            return entry;
        }

        return null;

    }

    default V putFirst(K k, V v) {
        throw new UnsupportedOperationException();
    }

    default V putLast(K k, V v) {
        throw new UnsupportedOperationException();
    }

    default SequencedSet<K> sequencedKeySet() {

    }

    default SequencedCollection<V> sequencedCollection() {

    }

    default SequencedSet<MyMap.Entry<K, V>> sequencedEntrySet() {

    }
}
