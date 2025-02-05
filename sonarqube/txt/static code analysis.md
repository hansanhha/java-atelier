[⟵](../README.md)

[static code analysis](#static-code-analysis)

[code smell](#code-smell)

[sonarqube](#sonarqube)


## static code analysis

정적 코드 분석 도구는 소스 코드를 실행하지 않고도 **코드의 버그**, **보안 취약점(vulnerability)**, **코드 스멜(code smell)** 등을 자동으로 분석하여 개발자가 품질을 유지하면서 코드를 작성할 수 있도록 돕는다

주로 ci/cd 파이프라인에 통합되어 코드가 배포되기 전에 품질 검사를 수행하는 역할을 한다

### 코드 품질 분석

코드에서 버그, 보안 취약점, 코드 스멜을 감지하여 보고한다

코드 중복, 코드 복잡도, 테스트 커버리지 등의 메트릭을 분석한다

### 정적 코드 분석

실행하지 않고 소스코드만 분석하여 문제를 탐색한다

e.g) 보안 취약점(sql injection, xss), 메모리 누수 가능성, 코드 스타일 위반 사항 등

### ci/cd 파이프라인 통합

github actions, jenkins 등의 빌드 시스템과 연동하여 코드 품질 검사를 자동화할 수 있다

### 품질 게이트

코드 품질 기준을 정의하여 코드 분석 결과가 기준을 충족하지 않으면 프로젝트 배포를 막을 수 있다

e.g) 테스트 커버리지 80% 이상 등의 정책 설정

### 대표 정적 코드 분석 도구

sonarqube

pmd: 자바 정적 코드 분석 도구

findbugs (spotbugs): 자바 코드 버그 탐지 도구

checkstyle: 자바 코드 스타일 검사

eslint: 자바스크립트 코드 스타일 및 린팅 분석

codeql: github 보안 분석 도구(sast)


## code smell

코드 스멜은 즉각적인 버그는 아니지만 유지보수성과 확장성을 저하시킬 가능성이 있는 코드를 의미한다

냄새나는 코드 유형
- 중복 코드: 비슷한 로직을 여러 파일에서 동일하게 구현한 경우
- 긴 메서드: 하나의 메서드가 너무 길고 복잡한 경우
- 많은 매개변수: 메서드가 너무 많은 인자를 요구하는 경우
- 큰 클래스: 한 클래스가 너무 많은 역할을 수행하는 경우 
- 과도한 조건문: 복잡하거나 중첩된 if-else 문이 많은 경우


## sonarqube

프레임워크나 라이브러리가 아니라 독립적인 애플리케이션이므로 프로젝트 코드에 직접 포함되지 않으며 별도의 서버 형태로 동작한다

웹 기반 ui를 제공하며 프로젝트 코드 품질을 분석한 후 결과를 시각적으로 확인할 수 있다

자바 뿐만 아니라 파이썬, 자바스크립트, C 등 다양한 언어를 지원한다

### sonarqube 핵심 개념

| 개념               | 설명                                           |
|------------------|----------------------------------------------|
| rules            | 코드 분석을 위한 정적 규칙 e.g) 변수를 사용하지 않으면 경고 발생      |
| issues           | 코드에서 발견된 문제 (bug, vulnerability, code smell) |
| quality profiles | 코드 분석 시 적용할 규칙 모음                            |
| quality gates    | 코드 품질 기준, 미달 시 빌드 실패 설정 가능                   |
| metrics          | 코드 품질 지표 (복잡도, 중복도, 테스트 커버리지 등)              |

