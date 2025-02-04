[⟵](../README.md)

[architecture](#architecture)

[workflow](#workflow)

[ci/cd integration](#cicd-integration)


## architecture

sonarqube는 클라이언트-서버 모델로 구성되어 있으며 크게 sonarqube server와 sonarqube scanner로 나눠진다

### sonarqube server

sonarqube 웹 애플리케이션
- tomcat, jetty 같은 자바 기반 웹 서버로 관리자가 대시보드에 접속하고 프로젝트 코드 품질 상태를 시각적으로 확인할 수 있는 기능을 제공한다

데이터베이스
- 프로젝트 분석 결과와 메타데이터를 저장하기 위한 관계형 데이터베이스를 사용한다

api
- sonarqube는 rest api를 제공하여 외부 시스템이나 도구에서 sonarqube에 접근하고 프로젝트 데이터를 가져오거나 설정을 변경할 수 있다
- api를 통해 코드 품질 데이터 및 분석 결과를 자동화된 시스템(ci/cd)에서 활용할 수 있다

### sonarqube scanner

sonarqube scanner는 sonarqube 서버에 분석 요청을 보내고 분석 결과를 받아오는 역할을 한다

다양한 환경에 맞는 여러 종류의 스캐너를 지원한다

sonarscanner: 자바 기반의 일반적인 sonarqube 분석 도구로 빌드 도구와 통합하지 않고 독립적으로 사용할 수 있다

maven/gradle scanner: maven/gradle과 통합되어 빌드 도구 중에 sonarqube 분석을 실행할 수 있도록 지원한다

jenkins plugin: jenkins와 통합되어 ci/cd 파이프라인 내에서 자동으로 코드 분석을 실행할 수 있다

### sonarqube plugins

language analyzers
- sonarqube는 자바 이외에도 여러 프로그래밍 언어(파이썬, 자바스크립트 등)에 대해 코드 분석을 수행할 수 있는 플러그인을 제공한다
- 언어별 분석기는 해당 언어의 코드 패턴을 파악하고 분석 규칙을 적용하여 분석 결과를 생성한다

기타 플러그인
- 보안 분석, 코드 커버리지, 빌드 도구 통합 등의 플러그인을 지원하여 사용자가 필요에 맞게 기능을 확장할 수 있다


## workflow

sonarqube의 작업 흐름은 크게 **코드 분석**과 **결과 시각화**로 나눠진다

### 코드 분석 흐름

#### 1. 프로젝트 설정

sonarqube를 설정하려면 sonarqube 서버에 프로젝트를 등록해야 한다

프로젝트가 등록되면 sonarqube는 해당 프로젝트에 대한 코드 분석을 설정할 수 있게 된다

이 때 quality profiles (코드 분석 규칙 모음), quality gates (코드 품질 기준) 등을 설정하여 품질 기준을 정의한다

#### 2. sonarscanner 실행 

개발자는 SonarScanner, 빌드 도구 플러그인 등과 언어별 분석기를 통해 다양한 언어의 문법과 규칙을 기반으로 프로젝트의 코드를 분석한다

SonarScanner는 프로젝트의 소스 코드를 읽고 분석을 시작하면 코드의 메트릭(코드 복잡도, 중복도, 테스트 커버리지, 규칙 위반 사항 등)을 수집하고 분석한다

분석을 마치면 sonarqube server에 결과를 전달한다

#### 3. sonarqube server 결과 처리

sonarqube server는 sonarqube scanner로부터 받은 결과(코드 품질 메트릭, 규칙 위반 사항, 버그 및 보안 취약점 등)를 처리하고 이를 데이터베이스에 저장한다

#### 4. 분석 결과 확인

sonarqube 웹 ui에서 분석 결과를 확인할 수 있다

대시보드를 통해 전체 프로젝트의 품질 상태를 한 눈에 볼 수 있으며 프로젝트의 문제점(버그 취약점, 코드 스멜 등)을 세부적으로 파악할 수 있다

#### 5. 품질 게이트 및 리포트

품질 기준을 설정한 경우 분석 결과가 quality gate를 통과해야만 배포가 가능하다

코드 분석 후 sonarqube는 품질 게이트에 따라 결과를 처리하고 통과 여부를 판단한다

#### 6. 문제 해결 및 개선

개발자는 sonarqube에서 발생한 이슈를 확인하고 해결하는 작업을 진행한다 -> 코드 품질 개선


## ci/cd integration

sonarqube는 ci/cd 파이프라인과 통합된다

jenkins와 통합하여 빌드 및 배포 전에 자동으로 코드 품질 분석을 실행할 수 있다

github actions, gitlab ci 등과 연동하여 코드가 푸시되면 자동으로 코드 분석을 실행하고 결과를 피드백할 수 있다

또한 sonarqube는 빌드가 성공적으로 완료되기 전에 품질 기준을 통과했는지 확인하고, 통과하지 못하면 배포가 실패하도록 설정할 수 있다





