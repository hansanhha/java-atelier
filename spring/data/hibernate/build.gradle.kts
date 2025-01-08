plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {

    implementation(libs.hibernate.orm)

    implementation(libs.hibernate.validator)
    implementation(libs.glassfish.el)

//    implementation libs.hibernate.agroal
//    implementation libs.agroal

    implementation(libs.log4j)

    annotationProcessor(libs.hibernate.jpamodelgen)

    runtimeOnly(libs.h2)
}

