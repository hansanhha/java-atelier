plugins {
    id("java")
}

group = "com.hansanhha"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.3")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}