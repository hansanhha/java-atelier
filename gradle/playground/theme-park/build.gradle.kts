plugins {
    application
}

application {
    mainClass.set("com.gradle.playground.RideStatusService")
}

repositories {
    mavenCentral()
}

tasks.register<JavaExec>("runJar") {
    group = "application"
    description = "Run the jar file itself by gradle"

    classpath(tasks.named("jar").map { it.outputs })
    classpath(configurations.runtimeClasspath)
    args("    teacups    ")

    mainClass.set("com.gradle.playground.RideStatusService")
}

//tasks.named<Jar>("jar") {
//    manifest {
//        attributes["Main-Class"] = "com.gradle.playground.RideStatusService"
//    }
//}

// compileJava, compileTestJava task configuring
tasks.withType<JavaCompile> {
    options.isVerbose = true
}

// processResources task configuring
tasks.named<Copy>("processResources") {
    include("**/*.txt")
}

group = "com.gradle.theme_park"
version = "0.1.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)
    implementation(libs.apache.commons.lang3)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

val rideFailureTest = "**/RideStatusServiceFailureTest.class"

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        register<JvmTestSuite>("integrationTest") {
            dependencies {
                implementation(project())
            }
        }
    }
}

tasks.check { dependsOn("integrationTest") }

