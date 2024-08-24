package com.hansanhha.types.generics;

public interface Calculator<@Operator T> {
    T add(T t1, T t2);
}
