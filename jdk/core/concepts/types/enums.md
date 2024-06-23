## Enum Classes

An enum declaration specifies a new enum class, a restricted kind of class that defines a small set of named class instance

<u>An enum class has no instance other than those defined by its enum constants</u>

The `equals` method in Enum is a final method that merely invokes super.equals on its argument and returns the result, thus performing an identity comparison.

**Enum declaration Rules**
- Enum class can be declared in top level, member, local
- Enum declaration can't have the modifier abstract, final, sealed and non-sealed
- A nested enum class **implicitly static** - every member enum and local enum class is static

**Direct Superclass of Enum and `extends` clause**
- The direct superclass type of enum class E is `Enum<E>`
- An enum declaration does not have an extends clause, so it is not possible to explicitly declare a direct superclass type, even Enum<E>

```text
{ClassModifier} enum TypeIdentifier [Class Implements]

    EnumBody
```

```java
public interface StatusManager<T> {

    T nextStatus(T currentStatus);
}

public enum DeliveryStatusManager implements StatusManager<DeliveryStatusManager> {

    PREPARING,
    READY,
    DELIVERING,
    DELIVERED;

    @Override
    public DeliveryStatusManager nextStatus(DeliveryStatusManager currentStatus) {
        return ...;
    }
}
```

## Enum Constants

An enum constant defines an instance of the enum class

**Enum Constant Rules**
- The Identifier in an EnumConstant provides the name of an implicit field of the enum class that can be used to refer to the enum constant
- The Optional class body of an enum constant implicitly declares anonymous class that is a direct subclass of enclosing enum class, and is final 

```text
EnumConstant:
    {EnumConstantModifier} Identifier [(Argument List)] [Class Body]
    
Argument List:
    Expression, {, Expression}
```

```java
public enum DeliveryStatusManager implements StatusManager<DeliveryStatusManager> {

    PREPARING("준비 중") {
        @Override
        public DeliveryStatusManager nextStatus(DeliveryStatusManager currentStatus) {
            return READY;
        }
    },

    ...
```



