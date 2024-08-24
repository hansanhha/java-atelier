[Common Usage of Annotations](#common-usage-of-annotations)

[Annotation Interface](#annotation-interface)

[Built-in Annotations](#built-in-annotations)

[Annotations](#annotations)

[Annotation Declaration](#annotation-declaration)

Annotations were added in java se 5, an annotation is used to provide additional information about program element

These do not directly influence the execution of the program, but I think they can see indirectly affect it when used by certain elements in the execution context      

In other words, it only provides information, the actual processing is done by the other component

## Common Usage of Annotations

**Documentation**
- it can be used to provide information documentation for program elements

**Code Generation**
- It also used to generate code, such as @Getter, @Setter of lombok that used to leaves boilerplate code out

**Runtime processing**
- It also can be processed at runtime
- This can be used to control the behavior of the program or provide information to the system

## Annotation Interface

### Annotation Interface Declaration

Actually annotation is a specialized kind of interface and annotation is instance of annotation interface

Annotation interface declaration specifies an annotation interface 

To distinguish from a normal interface declaration, the keyword interface is preceded by an at sign(@)

Note that at sign(@) and the keyword interface are distinct tokens - It's possible to separate them with whitespace

```text
{InterfaceModifier} @interface TypeIdentifier
    annotation interface Body
```

### Annotation Interface Rules

1. An annotation interface declaration may specify a top level interface or member interface, but not a local interface, and it can't appear in the body of local class/interface/anonymous class
  - Unlike a normal interface declaration, an annotation interface declaration **cannot declare any type variables(generic)** by virtue of the AnnotationTypeDeclaration production

2. The direct superinterface type of an annotation interface is **always** java.lang.annotation.Annotation
  - Unlike a normal interface declaration,  an annotation interface declaration cannot choose the direct superinterface type via an `extends` clause by virtue of the AnnotationTypeDeclaration production

3. An annotation interface **inherits** several methods from java.lang.annotation.Annotation, including the implicitly declared methods corresponding to the instance methods of `Object`
  - These methods do not define elements of the annotation interface
  - As a result annotation interface **ensures** that elements were of the types representable in annotations

```java
public interface Annotation {
    
    boolean equals(Object obj);
    
    int hashCode();
    
    String toString();

    Class<? extends Annotation> annotationType();
}
```

### Annotation Interface Element

The body of an annotation interface declaration may contain method declarations, each of which defines an element of the annotation interface. 

**An annotation interface has no elements other than those defined by the methods declared explicitly in the annotation interface declaration.**

Annotation interface element declaration is as follows, Annotation element modifier can be Annotation, public or abstract
```text
{Annotation element modifier} UnannotatedType Identifier ( ) [Dims] [DefaultValue] ;
```

Moreover, annotation interface has other member declaration and can return them
- Constant
- Class
- Interface

for example using enum member declaration:
```java
@interface Quality {
    enum Level { BAD, INDIFFERENT, GOOD }
    Level value();
}
```

**Annotation interface element grammar rules**
- A method declaration in an annotation interface declaration cannot have formal parameters, type parameters, or a throws clause
- also cannot be private, protected, or static -> cannot have the same variety of methods as a normal interface
- Cannot override method declared class `Object` or in interface `java.lang.annotation.Annotation`
- Annotation interface declaration cannot contain annotation interface type itself

**The return type of a method declared in the body of annotation interface**
- A primitive type
- String
- Class or an invocation of Class
- An enum class type
- An annotation interface type
- An array type whose component type is one of the preceding types

```java
@interface Watch {
    String brand;
    int price;
    String time;
}
```

```java
@interface Author {
    Name value();
}

@interface Name {
    String first();
    String last();
}
```

**default value of annotation interface element**
- annotation interface element may have a default value
- default values are not compiled into annotations, but rather applied dynamically at the time annotation are read
- Thus, changing a default value affects annotation even in classes that were compiled before the change was made
- types that can be element value of default value:
  - Conditional Expression
  - Element Value Array Initializer
  - Annotation

```java
@interface RequestForEnhancement{
    int id();          // No-default - must be specified in each annotation
    String synopsis(); // No-default - must be specified in each annotation
    String engineer()     default "[unassigned]";
    String date()         default "[unimplemented]";
    String[] status()     default {"requested", "approved", "rejected"};
}
```


### Marker Annotation Interface

An annotation with no elements is called a **marker annotation interface**

```java
@interface Component {}
```

### Single-element Annotation Interface

An annotation with single element is called a **single-element annotation interface**

By convention, the name of sole in single element annotation interface is `value` 

Thanks to linguistic support, do not need to specify element name when using single-element annotation

```java
@interface Copyright {
    String value();
}

@Copyright("hansanhha")
```

```java
@interface JavaUsers {
    String[] value();
}
```

```java
interface Formatter {}

@interface PrettyPrinter {
    Class<? extends Formatter> value();
}
```

### Repeatable Annotation Interface

## Built-in Annotations

### Target, Retention Policy

All annotations have a target and retention annotation that is built in

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Target {
    
    ElementType[] value();
}
```

**@Target** is used to provides information about where the applied annotation can be applied in the code

it has an element that returns ElementType[] to specify certain element types

list of element types
- TYPE, METHOD, CONSTRUCTOR, FIELD, PARAMETER, ANNOTATION_TYPE
- RECORD_COMPONENT, TYPE_PARAMETER, TYPE_USE
- PACKAGE, LOCAL_VARIABLE, MODULE

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Retention {
    
    RetentionPolicy value();
}
```

**@Retention** is used to provides information about when to use the applied annotation

it has an element that returns RetentionPolicy to specify certain retention policy

list of retention policies
- SOURCE
- CLASS
- RUNTIME

### @Inherited

The annotation interface `java.lang.annotation.Inherited` is used to indicate that annotations on a class C corresponding to a given annotation interface are inherited by subclasses of C.

```java
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@interface Foo {}
```

```java
@Foo
public class Parent{} 

// inherited @Foo annotation interface
public class Child extends Parent {}
```

## Annotations

An annotation is a marker which associates with a program element, but **no effect at runtime**

An **annotation** denotes a **specific instance** of an annotation interface and usually provides values for the element of that interface

There are three kinds of annotations:
- Normal Annotation
- Marker Annotation
- Single Element Annotation

### Normal Annotation

A normal annotation specifies the name of an annotation interface and optionally a list of comma-separated `element-value` pairs

Each pair contains an element value that is associated with an element of the annotation interface

**Normal Interface**
- `@TypeName([Element Value Pair List])`

**Details**
- Element Value Pair List: `Element Value Pair {, Element Value Pair}`
- Element Value Pair: `Identifier = ElementValue` (identifier is method of the annotation interface and return type of this defines the element type of the element value)
- Element Value
  - Conditional Expression
  - Element Value Array Initializer
  - Annotation

**ElementType(T) And ElementValue(V) Correspondence Rules**
- T is not array type, and the type of V is assignment compatible with T, and:
  - If T is a primitive type or String, then V is a constant expression
  - If T is Class or an invocation of Class, then V is a class literal
  - If T is an enum class type, then V is an enum constant
  - V is not null

**Element Value Pair Rules**
- They must contain element-value pair for every element of the annotation interface
  - **expect** for those elements with default values

```java
@RequestForEnhancement(
    id       = 2868724,
    synopsis = "Provide time-travel functionality",
    engineer = "Mr. Peabody",
    date     = "4/1/2004"
)
public static void travelThroughTime(Date destination) { 
     
}
```

### Marker Annotation

A marker annotation is a shorthand designed for use with [marker annotation interface](#marker-annotation-interface) 

**Marker Annotation**
- `@TypeName()`

```java
@Preliminary 
public class TimeTravel { 
    
}
```
 
Note that it can be used as marker annotation, even though annotation interface has multiple elements, if all elements of annotation interface have default value  

### Single Element Annotation

A single annotation is a shorthand designed for use with [single-element annotation interface](#single-element-annotation-interface)

**Single Annotation**
- `@TypeName (ElementValue)`

Note that it can be used as single element annotation, even though annotation interface has multiple elements, if one element is named `value` and all other elements have default value

```java
@Endorsers("Epicurus")
public class Pleasure {
    
}
```

```java
@Author(@Name(first = "Joe", last = "Hacker"))
public class BitTwiddle {
    
}
```

### Meta Annotation

An annotation that is declared annotation interface declaration

An annotation of interface A may appear as a meta-annotation on the declaration of the interface A itself. 

More generally, circularities in the transitive closure of the "annotates" relation are permitted.

For example, it is legal to annotate the declaration of an annotation interface S with a meta-annotation of interface T, and to annotate T's own declaration with a meta-annotation of interface S. 

The predefined annotation interfaces contain several such circularities.

### Composite Annotation



## Annotation Declaration

A **declaration annotation** is an annotation that applies to a declaration, and whose annotation interface is applicable in the declaration context represented by that declaration

or, a **type annotation** that applies to a type(or any part of type or array), and whose annotation interface is applicable in type contexts

```java
// Field annotation declaration
@Foo int f;
```

`@Foo` is a **declaration annotation** if Foo is meta-annotated by `@Target(ElementType.FIELD)` and a **type annotation** if Foo is meta-annotated by `@Target(ElementType.TYPE_USE)`

In addition to it is possible for @Foo to be both a declaration annotation and a type annotation simultaneously

```java
@A int @B [] @C[] f;
```

`@A` applies to the element type `int`

`@B` applies to the array type `int[][]`

`@C` applies to its component type `int[]`

It is possible for an annotation to appear at a syntactic location in a program where it could plausibly apply to a declaration or a type or both

This can happen in any of the six declaration contexts where modifiers immediately precede the type of the declared entity
- Method declarations (including elements of annotation interface)
- Constructor declarations
- Field declarations (including enum constants)
- Formal and exception parameter declarations
- Local variable declarations
- Record component declarations

Whether an annotation applies to the declaration or to the type declared entity(whether the annotation is a declaration annotation or type annotation)

depends on the applicability of the annotation's interface
- If the annotation's interface applicable in the declaration context corresponding to the declaration, and not in type context, then the annotation is deemed to apply only to the declaration
- If the annotation's interface applicable in the type context, and not in the declaration context corresponding to the declaration, then the annotation is deemed to apply to the type which is closet to the annotation
- If the annotation's interface applicable in the declaration context corresponding to the declaration, and in type context, then the annotation is deemed to apply both the declaration and the type which is closet to the annotation


## Annotation abstraction

## Annotation processor


https://webfuse.in/blogs/java-annotations-a-practical-guide-for-beginners/

https://docs.oracle.com/javase/specs/jls/se22/html/jls-9.html#jls-9.6