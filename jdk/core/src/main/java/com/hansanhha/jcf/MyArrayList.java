package com.hansanhha.jcf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MyArrayList<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private static final Object[] EMPTY_ELEMENTS = {};

    Object[] elements;

    private int size;

    public MyArrayList() {
        elements = EMPTY_ELEMENTS;
    }

    public MyArrayList(int capacity) {
        if (capacity <= 0) {
            elements = EMPTY_ELEMENTS;
            return;
        }

        elements = new Object[capacity];
    }

    public MyArrayList(Collection<? extends E> c) {
        var array = c.toArray();

        if ((size = array.length) > 0) {
            if (c.getClass() == ArrayList.class) {
                elements = array;
            } else {
                elements = Arrays.copyOf(array, size, Object[].class);
            }
        } else {
            elements = EMPTY_ELEMENTS;
        }

    }

    private Object[] grow(int minCapacity) {
        var oldCapacity = elements.length;

        if (oldCapacity > 0 || elements != EMPTY_ELEMENTS) {
            var newCapacity = adjustLength(oldCapacity, minCapacity - oldCapacity, oldCapacity >> 1);
            return elements = Arrays.copyOf(this.elements, newCapacity);
        }

        return new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
    }

    private Object[] grow() {
        return grow(size + 1);
    }

    private int adjustLength(int oldLength, int minGrowth, int preferGrowth) {
        var newLength = oldLength + Math.max(minGrowth, preferGrowth);

        if (newLength > 0 && preferGrowth < Integer.MAX_VALUE - 8) {
            return newLength;
        }

        newLength = oldLength + minGrowth;
        if (newLength < 0) {
            throw new OutOfMemoryError("Required array length " + oldLength + " + " + minGrowth + " is too large");
        } else {
            return newLength;
        }
    }
}
