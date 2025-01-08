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
    implementation(libs.spring.boot.test)
    implementation(libs.spring.boot.data.jpa)
    implementation(libs.spring.boot.web)
    runtimeOnly(libs.h2)
}

