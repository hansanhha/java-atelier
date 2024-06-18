package com.hansanhha.types.annotations;

public @interface Calculator {

    Predicate value() default @Predicate;
}
