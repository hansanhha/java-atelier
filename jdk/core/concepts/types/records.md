[Record](#record)

[Record Components](#record-components)

[Record Body](#record-body)

[Record Members](#record-members)

[Record Constructor Declaration](#record-constructor-declaration)

## Record

Record is a restricted kind of class that defines a "simple aggregate of values"

```text
Record Declaration:
    {Class Modifier} record TypeIdentifier [TypeParameters] RecordHeader [ClassImplements] RecordBody
```

**A Record Declaration Rules** 
- Record class can be declared in top-level, member, local
- Do not has the modifier "abstract", "sealed", "non-sealed"
- A record class is implicitly **final**
- A nested record class im implicitly **static**
- The direct superclass type of record class is `Record` - A record class does not have an `extends` clause, so it is not possible to explicitly declare a direct superclass 

## Record Components

The record components of a record class are values of record class and if any, are specified in the header of a record declaration

A record component corresponds to two members of the record class
- a "private field" declared implicitly
- a "public accessor method" declared explicitly or implicitly

```text
RecordHeader:
    ( {RecordComponentList} )
    
RecordComponentList:
    RecordComponent, {, RecordComponent}
    
RecordComponent:
    Annotation UnannotationType Identifier
    
    or 
    
    VariableArgumentRecordComponent

VariableArgumentRecordComponent:
    Annotation UnannotationType {Annotation} ... Identifier
```

**The Record Components Rules**
- Variable argument record component can be declared only once and placed last
- If the record component is not a variable argument record component, then the declared type is denoted by UnannotationType

## Record Body

The body of record declaration may contain constructor and member declaration as well as static initializers

```text
RecordBody:
    { {RecordBodyDeclaration} }
    
RecordBodyDeclaration:
    ClassBodyDeclaration
    CompactConstructorDeclaration
```

```text
ClassBodyDeclaration:
    ClassMemberDeclaration
    InstanceInitializer
    StaticInitializer
    ConstructorDeclaration
    
ClassMemberDeclaration:
    FieldDeclaration
    MethodDeclaration
    ClassDeclaration
    InterfaceDeclaration
```

**The Record Body Declaration Rules**
- The body of record declaration cannot contain the following:
  - non-static field
  - method declaration that is "abstract" or "native"
  - instance initializer

## Record Members

For each record component, a record class has a field with the same name as the record component and the same type as the declared type of the record component

This field, which is declared implicitly is known as a "component field"

**A component field features**
- A component field is "private", "field" and "non-static" 
- A component field is annotated with annotations, if any, that appear on the corresponding record component and whose annotation interfaces are applicable in the field declaration context or in type context or both
- For each component, a record class has a method with the same name as the record component and an empty formal parameter list (this method, which is declared implicitly or explicitly, is known as an "accessor" method)

**The explicitly declaring accessor method rules**
- The return type of the accessor method must be the same as the declared type of the record component
- The accessor method not be generic
- The accessor method must be a public instance method with no formal parameter list and no throws clause

**The implicitly declared accessor method properties following:**
- Its name is the same as the name of the record component
- Its return type is the same as the declared type of the record component
- It is not generic
- It is a public instance method with no formal parameters and no throws clause 
- It is annotated with the annotations, if any, that appear on the corresponding record component and whose annotation interfaces are applicable in the method declaration context, or in type contexts, or both
- Its body returns the value of the corresponding component field

## Direct Superclass Record

Every class of record type are implicitly extend `Record` class

```java
public abstract class Record {
    /**
     * Constructor for record classes to call.
     */
    protected Record() {}

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
```

A record class provides implementations of all the abstract methods declared in class Record. 

For each of the following methods, if a record class R does not explicitly declare a method with the same modifiers, name, and signature, then the method is implicitly declared as follows:


## Record Constructor Declaration

To ensure proper initialization of its record components, a record class does not implicitly declare a default constructor

Instead, a record class has **canonical constructor**, declared explicitly or implicitly, that initializes all the component fields of the record class

```java
public record Point(int x, int y) {
    
    // canonical constructor
    public Point(int x, int y) {
        
    }
} 
```

There are two ways to explicitly declare a canonical constructor in a record declaration
- Declaring a normal constructor with a suitable signature
- Declaring a compact constructor

## Normal Canonical Constructor

**Explicitly Declared Canonical Constructor Rules**
- Must provide at least as much access as the record class
  - record class(R) is public - canonical constructor(C) must be public
  - R is protected - C be protected or public
  - R is package   - C be package access, protected, or public
  - R is private   - C be declared with any accessibility
- Each formal parameter in the formal parameter list must have the same name and declared type as the corresponding record component
- The constructor body must not contain an explicit constructor invocation statement

```java
protected record Point(int x, int y) {
    
    // explicitly declared canonical constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
```

**If not explicitly declare Canonical Constructor**
- Canonical Constructor C is implicitly declared in record class R with the following properties
- The signature of C has not type parameter, and has formal parameters given by the derived formal parameter list of R
- C has the same access modifier as R
- C has no throws clause
- The body of C initializes each component field of R with the corresponding formal parameter list of C, in the order that record components appear in the record header 

**Non-canonical Constructor Rules**
- A record class may contain declaration of constructors that are not canonical constructors
- The body of them must start with an alternate constructor invocation

```java
public record Point(int x, int y) {
    
    private int calculate(int level, int point) {
        return level + point;
    } 
    
    public Point(int level, int x, int y) {
        // alternate constructor invocation (this constructor invocation)
        this(calculate(level, x), calculate(level, y));
    }
} 
```

## Compact Canonical Constructor

A compact constructor is a succinct form of constructor declaration, only available in a record declaration

The formal parameters of a compact constructor of record class are implicitly declared

The signature of a compact constructor is equal to the derived constructor signature of record class

**Compact Constructor Rules**
- must not contain a return statement
- must not contain an explicit constructor invocation statement

After last statement, all component fields of the record class are implicitly initialized to the value of the corresponding formal parameters

```java
record Rational(int num, int denom) {
    private static int gcd(int a, int b) {
        if (b == 0) return Math.abs(a);
        else return gcd(b, a % b);
    }
   
    Rational {
        int gcd = gcd(num, denom);
        num    /= gcd;
        denom  /= gcd;
    }
}

// The compact constructor Rational {...} behaves the same as this normal constructor:
Rational(int num, int denom) {
  int gcd = gcd(num, denom);
  num    /= gcd;
  denom  /= gcd;
  this.num   = num;
  this.denom = denom;
}
```

### compact constructor

normal constructor 

```
record Rectangle(double length, double width) {

    public Rectangle(double length, double width) {
        if (length < 5 || width < 5) {
            throw new IllegalArgumentException();
        }

        this.length = length;
        this.width  = width;
    }
}
```

compact constructor

```
record Rectangle(double length, doule width) {
    public Rectangle {
        if (length < 5 || width < 5) {
            throw new IllegalArgumentException();
        }
    }
}
```

매개변수, 할당문 생략

생성자 마지막 부분에서 매개변수가 field에 암시적으로 할당됨




