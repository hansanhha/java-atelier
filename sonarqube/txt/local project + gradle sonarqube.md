[⟵](../README.md)

[docker sonarqube 설정](#docker-sonarqube-설정)

[gradle 설정 및 사용](#gradle-설정-및-사용)


## docker sonarqube 설정

도커 이미지 pull

```shell
docker image pull sonarqube
```

sonarqube 실행

```shell
docker run --rm -d -p 9000:9000 --name sonarqube sonarqube
```

localhost:9000 접속

초기 아이디/비밀번호: admin


## gradle sonar task를 활용한 sonarqube 사용

### sonarqube 프로젝트 설정

sonarqube를 사용하려면 로컬에서 실행한 sonarqube 서버에 접속하여 프로젝트를 생성해야 한다

1. Create a local project 클릭 
2. project display name 설정 -> next 클릭 
3. set up project for clean as you code 설정  -> create project 클릭 
4. analysis method 선택 -> locally 클릭 
5. 토큰 이름/만료 기한 설정 generate -> continue 클릭

### gradle 설정 및 사용

#### 1. 플러그인 추가

```kotlin
plugins {
    java
    id("org.sonarqube") version("6.0.1.5171")
}
```

#### 2. sonar task 설정

```kotlin
sonar {
    properties {

        property("sonar.projectKey", "sonarqube-local-test")
        property("sonar.projectName", "sonarqube-local-test")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.token", "sqp_5bef6e063248f77a9e5038d0004897426bdf6504")

        // ci/cd 파이프라인 대신 gradle 빌드와 함께 사용하는 경우 환경 변수로 프로퍼티를 설정하는 게 더 안전하다
//        property("sonar.projectKey", System.getenv("SONAR_PROJECT_KEY"))
//        property("sonar.projectName", System.getenv("SONAR_PROJECT_NAME"))
//        property("sonar.host.url", System.getenv("SONAR_HOST_URL"))
//        property("sonar.token", System.getenv("SONAR_TOKEN"))


        // junit 리포트 위치
        property("sonar.junit.reportPaths", "build/test-results/test")

        // 분석 대상 제외 (특정 소스 코드 및 테스트 코드)
        property("sonar.exclusions", "**/generated/**")
    }
}
```

#### 3. gradle 빌드

아래의 gradle 명령어를 실행하고 sonarqube 서버(localhost:9000)에 접속하여 결과 확인 

```shell
./gradlew clean build sonar
```
