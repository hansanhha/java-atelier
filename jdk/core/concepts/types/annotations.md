Annotations were added in java se 5, an annotation is used to provide additional information about program element

These do not directly influence the execution of the program, but they can see indirectly affect it when used by certain elements in the execution context      

In other words, it only provides information, the actual processing is done by the other component

## Common Usage of Annotations

**Documentation**
- it can be used to provide information documentation for program elements

**Code Generation**
- It also used to generate code, such as @Getter, @Setter of lombok that used to leaves boilerplate code out

**Runtime processing**
- It also can be processed at runtime
- This can be used to control the behavior of the program or provide information to the system

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

### Other Built-in Annotation

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


#### Marker Annotation Interface

An annotation with no elements is called a **marker annotation interface**

```java
@interface Component {}
```

#### Single-element Annotation Interface

An annotation with single element is called a **single-element annotation interface**

By convention, the name of sole in single element annotation interface is `value` 

Thanks to linguistic support, do not need to specify element name to use when using single-element annotation

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

## Annotations

### Normal Annotations

### Meta Annotations

### Composite Annotation

## Annotation abstraction

## Annotation processor


https://webfuse.in/blogs/java-annotations-a-practical-guide-for-beginners/

https://docs.oracle.com/javase/specs/jls/se22/html/jls-9.html#jls-9.6