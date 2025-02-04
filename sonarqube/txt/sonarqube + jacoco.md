[⟵](../README.md)

[sonarquge + jacoco](#sonarqube--jacoco)

[gradle 설정](#gradle-설정)

[gradle 명령어 실행](#gradle-명령어-실행)


## sonarqube + jacoco

jacoco는 자바 코드의 테스트 커버리지를 측정하는 도구로 sonarqube가 jacoco의 테스트 커버리지 리포트를 활용하여 코드 품질을 분석하도록 설정할 수 있다


## gradle 설정

jacoco 플러그인 추가

```kotlin
plugins {
    java
    id("org.sonarqube") version("6.0.1.5171")
    jacoco
}
```

jacoco 플러그인의 jacocoTestReport task 설정


```kotlin
tasks.test {
    useJUnitPlatform()

    // jacoco task 순서 지정: gradle test -> jacoco report
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {

    reports {
        html.required.set(true)
        xml.required.set(true)
    }
}
```

sonarqube 플러그인의 sonar task 설정

```kotlin
sonar {
    properties {
        // jacoco 커버리지 리포트 경로 지정
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml").get().asFile.absolutePath
        )
    }
}
```

## gradle 명령어 실행

아래의 명령어를 실행하면 jacoco가 생성한 테스트 커버리지 분석 보고서를 기반으로 sonarquge의 코드 품질 분석을 수행한다

```shell
./gralew clean test sonar
```