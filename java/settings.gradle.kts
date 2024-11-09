plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "java"
include("types")
include("collection_frameworks")
include("jdk_tools")
