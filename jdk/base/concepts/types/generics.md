## Why use generic

Generics enable types to be parameters when defining classes, interface and methods

Type parameters provide a way for you to re-use the same code with different inputs  

The difference is that the inputs to formal parameters are values, while the inputs to type parameters are types

**Benefit of using generic**
- Stronger type checks at compile time
  - A java compiler applies strong type checking to generic code and issues error if the code violates type safety
- Eliminates of type casts
  - ```java
    // non-generic code
    List list = new ArrayList();
    list.add("hello");
    String s = (String) list.get(0);
    
    // using generic
    List<String> list = new ArrayList<String>();
    list.add("hello");
    String s = list.get(0);   // no cast
    ```
- Enabling programmers to implement generic algorithms


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

## Parameterized Types

A class or interface that is generic defines a set of parameterized types

**Parameterized Types form**
- interface/class TypeName<Type Arguments>
- Type Argument that denotes a particular parameterization of the generic class or interface

**Well-formed Parameterized Types**
- `Seq<String>`
- `Seq<Seq<String>>`
- `Seq<String>.Zipper<Integer>`
- `Pair<String, Integer>`

**Incorrect Parameterization**
- `Seq<int>` : primitive types cannot be type arguments
- `Pair<String>` : not enough type arguments
- `Pair<String, String, String>` : too many type arguments

### Type Arguments of Parameterized Types

Type argument are be either reference types or wildcards

Unlike ordinary [type variables](#type-variables) declared in a method signature, no type inference is required when using a wildcard

**Upper Bound Wildcard**

`extends ReferenceType` : `? extends B` (B is the upper bound)
- `? extends Object` is equivalent to the unbounded wildcard `?`

**Lower Bound Wildcard**

`? super RefercenType` : `? super B` (B is the lower bound)