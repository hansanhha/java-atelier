### 그레이들 주요 개념

Project : 전체 빌드 컨테이너

Build Script : 빌드 시 수행되는 스크립트

Tasks : 빌드 스크립트에 추가할 수 있는 개별적인 작업 유닛

Plugins : 유용한 빌드 기능을 제공하는 패키지된 컴포넌트

## Project

그레이들에서 Project는 빌드할 애플리케이션 전체를 나타내는 고수준 개념임(gradle build와 gradle project는 동의어)

스프링 부트 프로젝트가 있으면 Gradle Project에 포함되어 gradle 빌드 시 스프링 부트 애플리케이션을 빌드함

settings.gradle : 그레이들 프로젝트를 생성하는 파일

rootProject.name 속성에다가 프로젝트 이름을 지정할 수 있는데, 해당 프로젝트의 디렉토리와 다른 이름을 가져도 됨

필수 지정 값은 아니지만 일관된 프로젝트 이름을 유지할 수 있음

[step 1](../step-by-step/1.gradle-project)

## Build  Script 

프로젝트와 함께 무언가를 선언하거나 실행하고자 할 때 (애플리케이션 빌드) build script에 추가하면 됨

build script는 코드(groovy, kotiln)로 작성하기 때문에 유연하게 빌드 로직을 구성할 수 있음

보통 task, plugin, dependency 등을 추가함

[step 2](../step-by-step/2.build-script)

## Tasks

빌드 스크립트에 추가할 수 있는 개별적인 작업 유닛

여러 가지의 task를 추가해서 빌드 스크립트를 구성할 수 있음

task 사용 방법
- 미리 정의된 task 사용 :  task 인스턴스를 정의하고 어떻게 사용할지 구성하면 됨
- 커스텀 task 생성 및 사용

Copy task(Gradle에서 제공) 예시
```java
tasks.register<Copy>("copyHello") {
    from("source 파일 위치")
    into("복사할 위치")
}
```

[step 3](../step-by-step/3.tasks)

## Plugins

plugin은 미리 정의된 tasks, 도메인 객체, 컨벤션 등으로 구성된 컴포넌트로 빌드 스크립트에 추가할 수 있음

plugin을 gradle project에 추가하면 자동적으로 task들이 등록됨

크게 세 가지로 나뉨
- [gradle core plugins](https://docs.gradle.org/current/userguide/plugin_reference.html#plugin_reference) - java, application plugin 등
- [community plugins](https://plugins.gradle.org/) - org.springframework.boot plugin 등
- custom plugins - 직접 제작([Plugin API](https://docs.gradle.org/current/javadoc/org/gradle/api/Plugin.html))

예시
```java
plugins {
    java
}
```

[step 4](../step-by-step/4.plugins)