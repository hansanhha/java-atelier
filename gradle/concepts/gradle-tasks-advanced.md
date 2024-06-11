[예시](../step-by-step/3.tasks)

[Grouping Tasks](#grouping-tasks)

[Multiple task](#multiple-task)

[Default Task](#default-task)

[Abbreviated Task](#abbreviated-task)

[Command line options](#command-line-options)

[Defining tasks](#defining-task)

[Locating tasks](#locating-tasks)

## Grouping Tasks

직접 커스텀 task를 정의해본 뒤 `./gradlew tasks`를 실행해보면 정의한 task가 표시되지 않음

task의 group을 지정해야 볼 수 있는데, 지정되지 않은 모든 task를 보려면 `./gradlew tasks --all` 수행

task 그룹 지정
```kotlin
// build.gradle.kts
tasks.register<Copy>("testCopy") {
    group = "custom task"
    ...
}
```

기본 제공되는 task와 plugin으로 인해 추가된 task는 미리 그룹화되어 있음

group 확인 방법
- `./gradlew tasks`
- intelliJ 사용 시 gradle 툴 바를 보면 Tasks가 그룹 확인 가능

## Multiple task

gradle은 0개, 1개, 여러 개의 task를 수행할 수 있음

한 개의 task 수행 : `./gradlew task1`

여러 개 task 수행 : `./gradlew task1 task2`

## Default Task

기본 수행 task : help

```kotlin
// build.gradle.kts
defaultTasks("someTask")
```

## Abbreviated Task

task 이름을 축약해서 사용 가능(custom task도 가능)
- `./gradlew h` : `./gradlew help`

카멜 케이스로 단어 구분
- `./gradlew hW` : `./gradlew helloWorld`

약칭 충돌 시 gradle이 설명해줌 - 구분 지어질 정도로 약칭 사용하면 됨
- `./gradlew h` : `./gradlew help`, `./gradlew helloWorld` 충돌
- `./gradlew hW` 또는 `./gradlew hello`

## Command line options

[task 별 옵션 정리](https://fig.io/manual/gradle)

주요 옵션
- `./gradlew -h, --help` : 도움말 
- `./gradlew specificTask --info` : 특정 task에 대한 상세 정보
- `./gradlew specificTask --console=verbose` : gradle output text 포맷 지정
  - console 옵션 값 : auto(default), plain, rich, verbose

## Defining Task

Defining : task type, 로직 정의

task class
- task가 수행해야 될 실제 로직이 담겨있는 task 클래스
- Gradle 소스코드나 플러그인에 미리 정의되어 있는 task 클래스를 "built"라고 일컬음

task definition
- task 클래스의 인스턴스를 정의하는 과정(인스턴스 이름 지정)
- 정의할 task가 무슨 동작을 할지 구성함

task는 크게 두 가지로 나뉨
- **class-based task** : task class 기반 task definition
  - task가 제공하는 프로퍼티를 사용하여 task 동작 구성
- **ad-hoc task** : 커스텀 task class 기반 task definition

### task 정의 방법

**방법 1 : tasks.register**
- tasks.register()
- [build 시 실제로 사용할 task들만 생성 및 구성](gradle-build-lifecycle.md#configuration-avoidance-buildup-to-date)됨

```kotlin
tasks.register<Copy>("generateApiDocs") {
}
```

- tasks.registering() - 변수 할당

```kotlin
val generateApiDocs by tasks.registering(Copy::class)
```

두 방법 모두 class-based task 또는 ad-hoc task을 정의할 수 있음

```kotlin
// class-based
tasks.register<Copy>("generatedApiDocs") {
    from(...)
    into(...)
}

// ad-hoc
tasks.register("greeting") {
  doLast {
    println("Hello World")
  }
}
```

**방법 2 : task()**

원래 사용하던 방법으로 지금은 tasks.register()로 대체됨

```kotlin
task<Copy>("generateApiDocs")
```

### task 정의 프로퍼티

task를 정의할 때 [람다식](./kotlin.md#람다식)을 사용하여 함수 또는 프로퍼티 셋을 전달함

람다에 사용하는 프로퍼티들은 정의하고 있는 task 타입에 의존함 - i.g) copy 타입의 task 정의시 from(), into() 사용

공통 프로퍼티
- group
- description
- doFirst (lambda)
- doLast (lambda)
- enabled
- onlyIf

**doFirst, doLast** 

task 실행 전, 실행 후 전달받은 람다 실행
```kotlin
tasks.register<Copy>("generateApiDocs") {
    doFirst {
        println("before generate api docs")
    }
  
    doLast {
        println("after generate api docs")
    }
}
```

**enabled**

task 실행 여부를 결정할 수 있음

기본 값 true

```kotlin
tasks.register<Copy>("coding") {
    enabled = false
}
```

**onlyIf**

특정 조건이 참일 때 task 실행

```kotlin
tasks.register<Copy>("coding") {
    onlyIf {
        // condition
    }
}
```

## Locating Tasks

Locating : task reference를 가져온 뒤, 로직을 수정하는 방법

**방법 1 : tasks.named**

```kotlin
tasks.named<Copy>("generateApiDocs") {
  doFirst {
    println("doFirst modified by locating task")
  }
}
```

named()는 TaskProvider를 반환함 

TaskProvider는 생성되지 않은 task에 대한 reference를 가지고 있음

```java
// method definition
TaskProvider<T> named(java.lang.String name, Action<? super T> configurationAction) throws UnknownTaskException
```