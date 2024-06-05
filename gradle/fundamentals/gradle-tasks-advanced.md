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

## 커맨드 라인에서 task 수행 방법

gradle은 0개, 1개, 여러 개의 task를 수행할 수 있음

한 개의 task 수행 : `./gradlew task1`

여러 개 task 수행 : `./gradlew task1 task2`

## 기본 수행될 task 지정

기본 수행 task : help

```kotlin
// build.gradle.kts
defaultTasks("someTask")
```

## task 약칭

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


