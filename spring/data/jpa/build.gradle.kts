plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.management)
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(libs.spring.boot)
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.data.jpa)

    testImplementation(libs.spring.boot.test)
    testImplementation(libs.jupiter)

    annotationProcessor(libs.lombok)

    runtimeOnly(libs.h2)
    runtimeOnly(libs.spring.boot.actuator)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}