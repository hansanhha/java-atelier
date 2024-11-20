[AnnotatedTypeMetadata](#annotatedtypemetadata)

## AnnotatedTypeMetadata

AnnotattedTypeMetadata는 클래스나 메서드, 필드에 적용된 어노테이션 메타데이터를 나타냄

[MergedAnnotations](./annotation.md/#mergedannotations)

**AnnotatedTypeMetadata 인터페이스**
```java
public interface AnnotatedTypeMetadata {
    
    MergedAnnotations getAnnotations();
    
    // default, static methods
}
```

