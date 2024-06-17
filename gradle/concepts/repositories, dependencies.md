## Repository

Repository
- 의존성을 가져올 공개 호스팅 온라인 서비스
- Maven Central, Google Maven을 지원하고 있음
- build script에 repository 명시 필요

resolve dependency
- gradle이 build script에 명시된 의존성을 확인하고 repository로부터 fetch하는 의존성 해결프로세스

**repository 선언**

gradle은 resolve 시 명시한 순서대로 의존성을 repository에서 찾음 

```kotlin
repositories {
    mavenCentral()
    google()
    maven {
        url = uri("https://custom-repo.com")
    }
}
```
## Dependency

Dependency : build script에 명시한 project build 시 필요한 의존성(artifact)들

**repository format**
- Maven(Maven Central, Google Maven)
- Ivy
- flat directory(local storage)

**.pom file that is describe project** 

maven repository에 publish되는 artifact는 .pom 파일에 프로젝트에 대한 정보를 설명해놓음

gradle은 해당 정보를 통해 의존성을 가져옴(dependency coordinate)

또한 해당 파일에 transitive 의존성이 명시되어 있는데 gradle은 전체 의존성을 파악하여 모두 가져옴

- group id
- name(artifact id)
- version

**trasitive dependency**
- artifact가 의존하는 다른 artifact

## Dependency Specifying, Configuration

**dependency configuration**

dependency configuration은 의존성이 어느 classpath에 필요한지 그룹화하는 데 사용됨

implementation : compile, runtime classpath에 추가



**특정 의존성 모듈 제외 명시 방법**

참고사항 : transitive 의존성을 제외하는 경우 해당 의존성을 필요로 하는 의존성마다 exclude를 명시 해줘야 됨  

```kotlin
implementation("group:artifact:version")

implementation(group = "group", name = "artifact", version = "version")

// transitive dependency 제외
implementation("group:artifact:version") {
    exclude(group = "group", module = "module")
}
```

**classpath**

프로젝트를 빌드하는 동안 여러 단계를 거침

`컴파일 -> 테스트 -> 실행`

자바의 classpath는 애플리케이션을 컴파일하거나 실행할 때 jvm에게 전달되는 파일 리스트임

각 단계마다 필요한 의존성이 다름

그래서 상황에 맞게 **complile classpath**, **runtime classpath**로 나뉨