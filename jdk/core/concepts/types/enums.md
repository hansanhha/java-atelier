## Enum Classes

An enum declaration specifies a new enum class, a restricted kind of class that defines a small set of named class instance

**An enum class has no instance other than those defined by its enum constants**

also `equals` method in Enum is a final method that merely invokes super.equals on its argument and returns the result, thus performing an identity comparison.

**Enum declaration Rules**
- Enum class can be declared in top level, member, local
- Enum declaration can't have the modifier abstract, final, sealed and non-sealed
- A nested enum class **implicitly static** - every member enum and local enum class is static

**Direct Superclass of Enum and `extends` clause**
- The direct superclass type of enum class E is `Enum<E>`
- An enum declaration does not have an extends clause, so it is not possible to explicitly declare a direct superclass type, even Enum<E>

**enum declaration structure:**
```text
{ClassModifier} enum TypeIdentifier [Class Implements]

    EnumBody
```

**for example:**
```java
public interface StatusManager<T> {

    T nextStatus(T currentStatus);
}

// enum class
public enum DeliveryStatusManager implements StatusManager<DeliveryStatusManager> {

    // Enum Body
}
```

## Enum Body

Enum Body of Enum class consists two parts that enum constant declaration and enum body declarations

**enum body structure:**
```text
EnumBody:
    { [EnumConstantList] [,] [EnumBodyDeclarations] }
```

### Enum Constants

An enum constant defines an instance of the enum class

**Enum Constant Rules**
- The Identifier in an EnumConstant provides the name of an implicit field of the enum class that can be used to refer to the enum constant
- The Optional class body of an enum constant implicitly declares anonymous class that is a direct subclass of enclosing enum class, and is final
- Enum Constant can't have "final" keyword 

**enum constant structure:**
```text
EnumConstant:
    {EnumConstantModifier} Identifier [(Argument List)] [Class Body]
    
Argument List:
    Expression, {, Expression}
```

**for example:**
```java
public enum DeliveryStatusManager implements StatusManager<DeliveryStatusManager> {

    // enum constant
    PREPARING("준비 중") {
        @Override
        public DeliveryStatusManager nextStatus(DeliveryStatusManager currentStatus) {
            return READY;
        }
    },

    ...
```

### Enum Body Declarations

enum body may contain constructor and member declarations as well as instance and static initializers

**enum body structure:**
```text
EnumBodyDeclarations:
; {ClassBodyDeclaration}
```

**class body declaration and class member declaration structure:**
```text
ClassBodyDeclaration:
- ClassMemberDeclaration
- InstanceInitializer
- StaticInitializer
- ConstructorDeclaration


ClassMemberDeclaration:
- FieldDeclaration
- MethodDeclaration
- ClassDeclaration
- InterfaceDeclaration
;
```

**for example:**
```java
public enum DeliveryStatusManager implements StatusManager<DeliveryStatusManager> {

    PREPARING("준비 중") {
        @Override
        public DeliveryStatusManager nextStatus() {
            var now = LocalDateTime.now();
            PREPARING.finish.set(now);
            READY.start.set(now);
            return READY;
        }
    },

    private final String value;
    private ThreadLocal<LocalDateTime> start;
    private ThreadLocal<LocalDateTime> finish;
    private static final String PREFIX = "delivery: ";

    public String getStatus() {
        return PREFIX;
    }

    DeliveryStatusManager(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
```

**Enum Body Declarations Rules**
- Any constructor or member declarations in the body of an enum declaration apply to the enum class exactly as if they had been present in the body of a normal class declaration, unless explicitly stated otherwise
- Access modifier of constructor declaration in an enum declaration can't have "public" or "protected"
- In an enum declaration, a constructor declaration with no access modifier is "private"
- constructor declaration in an enum declaration do not contain superclass constructor invocation statement
- do not refer to a "static" field of an enum class from a constructor, instance initializer, or instance variable initializer in the enum declaration of the class
  - it can if field is a constant variable

## Enum Members

The members of an enum class E are all following:
- `Members` declared in the body of the declaration of E
- Members inherited from `Enum<E>`
- For each `enum constant` "c" declared in the body of the declaration of E
  - E has an implicitly declared `public static final` field of type E that has same name as "c"
- An implicitly declared method `public static E[] values`, which returns an array containing the enum constants of E, in the same order as they appear in the body of the Declaration of E
- An implicitly declared method `public static E valueOf(String name)`, which return the enum constant of E with the specified name
- Therefore, a declaration of E cannot contain anything that conflicts with E's enum constants and implicitly declared methods or override final method of class `Enum<E>`
