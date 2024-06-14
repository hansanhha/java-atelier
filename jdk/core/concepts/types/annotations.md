Annotations were added in java se 5, an annotation is used to provide additional information about program element

these do not directly influence the execution of the program, but they can indirectly affect it when used by certain elements in the execution context      

In other words, it only provides information, the actual processing is done by the other component

## Usage of annotation

Common use cases

**Documentation**
- Annotations can be used to provide information documentation for program elements

**Code Generation**
- It also used to generate code, such as @Getter, @Setter of lombok that used to leaves boilerplate code out

**Runtime processing**
- It also can be processed at runtime
- This can be used to control the behavior of the program or provide information to the system, user

## Target, Retention Policy

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Target {
    
    ElementType[] value();
}
```

@Target is used to provide where apply the annotation in the context

it has an element that returns ElementType[] to specify certain element types 

list of ElementType
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

this type of annotation called meta annotation

```java

```

## Annotation element

## Built-in annotation

## Marker annotation

## Meta annotation

## Composite annotation

## Annotation abstraction

## Annotation processor

## Annotation interface

```java
public interface Annotation {
    
    boolean equals(Object obj);
    
    int hashCode();
    
    String toString();

    Class<? extends Annotation> annotationType();
}
```


https://webfuse.in/blogs/java-annotations-a-practical-guide-for-beginners/