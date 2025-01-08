plugins {
    id("java")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.management)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot)
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.data.jpa)
    implementation(libs.spring.boot.security)
    implementation(libs.spring.boot.security.oauth2.client)
    implementation(libs.spring.boot.actuator)
    implementation(libs.spring.boot.thymeleaf)
    testImplementation(libs.spring.boot.test)
}
