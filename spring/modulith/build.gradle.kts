plugins {
    id("java")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.management)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.modulith.core)
    implementation(libs.spring.modulith.events.api)
    implementation(libs.spring.modulith.events.core)
    implementation(libs.spring.modulith.jpa)
    runtimeOnly(libs.spring.modulith.actuator)
    testImplementation(libs.spring.moduliith.test)

    implementation(libs.spring.boot.configuration.processor)
    implementation(libs.spring.boot.data.jpa)
    implementation(libs.spring.boot.web)
    runtimeOnly(libs.spring.boot.actuator)
    testImplementation(libs.spring.boot.test)

}