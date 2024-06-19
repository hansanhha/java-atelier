plugins {
    java
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "com.gradle.playground.RideStatusService"
    }
}

//tasks.named<JavaCompile>("compileJava") {
//    options.isVerbose = true
//}

// compileJava, compileTestJava task configuring
tasks.withType<JavaCompile> {
    options.isVerbose = true
}

// processResources task configuring
tasks.named<Copy>("processResources") {
    include("**/*.txt")
}

// jar task configuring
//tasks.named<Jar>("jar") {
//    archiveFileName.set("test.jar")
//}

group = "com.gradle.playground"
version = "0.1.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

sourceSets.main {
    java {
        srcDir("src/hansanhha/main/java")
    }
    resources {
        srcDir("src/hansanhha/main/resources")
    }
}

sourceSets.test {
    java {
        srcDir("src/hansanhha/test/java")
    }
    resources {
        srcDir("src/hansanhha/test/resources")
    }
}

val saveBuildInfo = "saveBuildInfo";
val buildInfoProperty = "buildInfo";

val getBuildInfo = tasks.register("getBuildInfo") {
    dependsOn("build")
    group = "build info"

    doFirst {
        println("Getting build info...")
    }
    doLast {
        val buildInfo = """
            Project Name: ${project.name}
            Gradle Version: ${gradle.gradleVersion}
            Java Version: ${JavaVersion.current()}
            Java Home: ${System.getProperty("java.home")}
        """.trimIndent()

        project.ext[buildInfoProperty] = buildInfo

        println("Build info")
        println(buildInfo)
    }
}

tasks.register<Copy>(saveBuildInfo) {
    group = "build info"

    dependsOn(getBuildInfo)

    doLast {
        val buildInfo = project.ext[buildInfoProperty] as String

        val tempDir = layout.buildDirectory.dir("temp")
        tempDir.map { it.file("build-info.txt") }.get().asFile.writeText(buildInfo)

        from(tempDir)
        into(layout.buildDirectory.dir("build-info"))
    }
}

