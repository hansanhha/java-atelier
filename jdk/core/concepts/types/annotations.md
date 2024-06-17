Annotations were added in java se 5, an annotation is used to provide additional information about program element

These do not directly influence the execution of the program, but they can see indirectly affect it when used by certain elements in the execution context      

In other words, it only provides information, the actual processing is done by the other component

## Common Usage of annotation

**Documentation**
- it can be used to provide information documentation for program elements

**Code Generation**
- It also used to generate code, such as @Getter, @Setter of lombok that used to leaves boilerplate code out

**Runtime processing**
- It also can be processed at runtime
- This can be used to control the behavior of the program or provide information to the system

## Built-in annotations

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
- TYPE
- FIELD 
- METHOD
- PARAMETER
- CONSTRUCTOR
- LOCAL_VARIABLE 
- ANNOTATION_TYPE
- PACKAGE
- TYPE_PARAMETER 
- TYPE_USE
- MODULE
- RECORD_COMPONENT

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

### Other built-in annotation

## Annotations

### Marker annotation

### Meta annotation

### Composite annotation

## About annotation

### Annotation interface

**Annotation interface declaration**

Actually annotation is a specialized kind of interface

And annotation interface declaration specifies an annotation interface 

To distinguish from a normal interface declaration, the keyword interface is preceded by an at sign(@)

Note that at sign(@) and the keyword interface are distinct tokens - It's possible to separate them with whitespace

```text
{InterfaceModifier} @interface TypeIdentifier
    AnnotationInterface Body
```

**Annotation interface rules**

- An annotation interface declaration may specify a top level interface or member interface, but not a local interface, and it can't appear in the body of local class/interface/anonymous class
  - Unlike a normal interface declaration, an annotation interface declaration **cannot declare any type variables** by virtue of the AnnotationTypeDeclaration production
- The direct superinterface type of an annotation interface is **always** java.lang.annotation.Annotation
  - Unlike a normal interface declaration,  an annotation interface declaration cannot choose the direct superinface type via an `extends` clause by virtue of the AnnotationTypeDeclaration production
- An annotation interface **inherits** several methods from java.lang.annotation.Annotation, including the implicitly declared methods corresponding to the instance methods of `Object`
  - these methods do not define elements of the annotation interface
  - as a result annotation interface **ensures** that elements were of the types representable in annotations

```java
public interface Annotation {
    
    boolean equals(Object obj);
    
    int hashCode();
    
    String toString();

    Class<? extends Annotation> annotationType();
}
```

### Annotation interface element



## Annotation abstraction

## Annotation processor


https://webfuse.in/blogs/java-annotations-a-practical-guide-for-beginners/

https://docs.oracle.com/javase/specs/jls/se22/html/jls-9.html#jls-9.6