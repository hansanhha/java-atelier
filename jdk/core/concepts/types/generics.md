## Type Parameter

**type parameter** : `{type parameter modifier} type identifier [type bound]`
- type parameter modifier : annotation (`<@Value E>`)
- type bound
  - extends type variable (`extends T`)
  - extends class or interface type {additional bound} (`extends String`)

```java
@Target(ElementType.TYPE_PARAMETER)
public @interface Operator {}

public class Calculator<@Operator T> {
    T add(T t1, T t2);    
}

```

## Type Variables

A type variables is **unqualified identifier used as a type** in class, interface, method and constructor bodies

It is introduced by the declaration of a type parameter of a generic class

Every type variable declared as a type parameter has a bound
- If no bound is declared for type variable, **Object** is assumed
- If a bound is declared, it consists of either
  - a single type variable `T`
  - a class or interface type `T` possibly followed by interface types l1 & ... ln

No bound type variables:
```java
public class C<T> {
    
    void test(T t) {
        
    }
}
```

A single type variables:
```java
public class C<T> {
    
    <T> void test(T t) {
        
    }
}
```
