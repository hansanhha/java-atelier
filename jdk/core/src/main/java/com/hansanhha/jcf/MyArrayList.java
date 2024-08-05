package com.hansanhha.jcf;

import java.util.*;

public class MyArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess {

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
        Object[] array = c.toArray();

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

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        Objects.checkIndex(index, size);
        return (E) elements[index];
    }

    @Override
    public int size() {
        return size;
    }

    private void add(E e, Object[] elements, int s) {
        if (s == elements.length) {
            elements = grow();
        }
        elements[s] = e;
        size = s + 1;
    }

    @Override
    public boolean add(E e) {
        add(e, elements, size);
        return true;
    }

    @Override
    public void add(int index, E e) {
        rangeCheckForAdd(index);

        final int s = size;
        Object[] elements = this.elements;

        if (s == elements.length) {
            elements = grow();
        }

        System.arraycopy(elements, index,
                elements, index + 1,
                s - index);

        elements[index] = e;
        size = s + 1;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] array = c.toArray();
        int numNew = array.length;

        if (numNew == 0) {
            return false;
        }

        Object[] elements = this.elements;
        final int s = size;
        if (numNew > elements.length - s) {
            elements = grow(s + numNew);
        }

        System.arraycopy(array, 0, elements, s, numNew);
        size = s + numNew;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);

        Object[] array = c.toArray();
        int numNew = array.length;

        if (numNew == 0) {
            return false;
        }

        final int s = size;
        Object[] elements = this.elements;

        if (numNew > elements.length - s) {
            elements = grow(s + numNew);
        }

        int numMoved = s - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + numNew, numMoved);
        }
        System.arraycopy(array, 0, elements, index, numNew);
        size = s + numNew;
        return true;
    }

    @Override
    public void addFirst(E e) {
        add(0, e);
    }

    @Override
    public void addLast(E e) {
        add(e);
    }

    @Override
    public E remove(int index) {
        Objects.checkIndex(index, size);
        final Object[] es = elements;

        @SuppressWarnings("unchecked")
        E oldValue = (E) es[index];
        fastRemove(es, index);

        return oldValue;
    }

    @Override
    public boolean remove(Object o) {
        final Object[] es = elements;
        final int size = this.size;
        int i = 0;

        found: {
            if (o == null) {
                for (; i < size; i++) {
                    if (es[i] == null)
                        break found;
                }
            } else {
                for (; i<size; i++) {
                    if (es[i].equals(o))
                        break found;
                }
            }
            return false;
        }

        fastRemove(es, i);
        return true;
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        Object[] es = elements;
        @SuppressWarnings("unchecked")
        E oldValue = (E) es[0];
        fastRemove(es, 0);
        return oldValue;
    }

    @Override
    public E removeLast() {
        int last = size - 1;

        if (last < 0) {
            throw new NoSuchElementException();
        }

        Object[] es = elements;
        @SuppressWarnings("unchecked")
        E oldValue = (E) es[last];
        fastRemove(es, last);
        return oldValue;
    }

    private void fastRemove(Object[] es, int index) {
        int newSize = size - 1;

        if (newSize > index) {
            System.arraycopy(es, index + 1, es, index, newSize - index);
        }

        es[size = newSize] = null;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return batchRemove(c, false, 0, size);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return batchRemove(c, true, 0, size);
    }

    private boolean batchRemove(Collection<?> c, boolean complement, final int start, final int end) {
        Objects.requireNonNull(c);

        final Object[] es = elements;
        int r = start;

        // 조건에 맞는 값이 있는지 확인하는 과정
        for (;; r++) {
            if (r == end) {
                return false;
            }

            if (c.contains(es[r]) != complement) {
                break;
            }
        }

        int w = r++;

        try {
            // 조건에 맞는 값들을 복사하는 과정
            for (Object e; r < end; r++) {
                if (c.contains(e = es[r]) == complement) {
                    es[w++] = e;
                }
            }
        } catch (Throwable ex) {
            System.arraycopy(es, r, es, w, end - r);
            w += end - r;
            throw ex;
        } finally {
            // 나머지 값들을 정리하는 과정
            shiftTailOverGap(es, w, end);
        }

        return true;
    }

    private void shiftTailOverGap(Object[] es, int lo, int hi) {
        System.arraycopy(es, hi, es, lo, size - hi);
        for (int to = size, i = (size -= hi - lo); i < to; i++) {
            es[i] = null;
        }
    }

    private Object[] grow(int minCapacity) {
        var oldCapacity = elements.length;

        if (oldCapacity > 0 || elements != EMPTY_ELEMENTS) {
            var newCapacity = adjustLength(oldCapacity, minCapacity - oldCapacity, oldCapacity >> 1);
            return elements = Arrays.copyOf(elements, newCapacity);
        }

        return elements = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
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

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}
