## Condition

@Conditional과 함께 사용되는 함수형 인터페이스로 특정 조건에 따라 해당 Bean을 등록하거나 무시하기 위해 사용됨

애플리케이션 로드 시점에 @Conditional이 적용된 클래스나 메서드에 대해서 Condition 구현체가 동작함

```
@FunctionalInterface
public interface Condition {
    boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);
}
```

ConditionContext : 현재 애플리케이션 컨텍스트와 관련된 정보(Environment, ResourceLoader, ClassLoader 등) 제공

AnnotatedTypeMetadata : 평가할 클래스나 메서드에 대한 메타데이터 제공

ProfileCondition의 경우 해당 클래스나 메서드에 적용된 Profile을 가져온 뒤, 현재 Environment의 Profile에 포함되는 경우에만 true를 반환하고 빈으로 등록됨

```
class ProfileCondition implements Condition {
    ProfileCondition() {
    }

    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
        if (attrs != null) {
            Iterator var4 = ((List)attrs.get("value")).iterator();

            Object value;
            do {
                if (!var4.hasNext()) {
                    return false;
                }

                value = var4.next();
            } while(!context.getEnvironment().matchesProfiles((String[])value));

            return true;
        } else {
            return true;
        }
    }
}
```

### Conditional

Bean 등록과 관련된 @Bean, @Configuration, @Component에 선언하는 어노테이션

@Profile 어노테이션에 @Conditional과 ProfileCondition이 선언되어 있음(메타 어노테이션)

```
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({ProfileCondition.class})
public @interface Profile {
    String[] value();
}
```

[스프링부트 Conditional]()
