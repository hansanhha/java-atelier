plugins {
    java
    application
    id("org.sonarqube") version("6.0.1.5171")
    jacoco
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

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


        // jacoco 커버리지 리포트 경로
        property("sonar.coverage.jacoco.xmlReportPaths", layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml").get().asFile.absolutePath)

        // junit 리포트 위치
        property("sonar.junit.reportPaths", layout.buildDirectory.dir("test-results/test").get().asFile.absolutePath)

        // 분석 대상 제외 (특정 소스 코드 및 테스트 코드)
        property("sonar.exclusions", "**/generated/**")
    }
}


application {
    mainClass = "hansanhha.SonarQubeApplication"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}

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