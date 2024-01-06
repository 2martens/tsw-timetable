plugins {
    id("twomartens.spring-boot-base")
    id("twomartens.kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(project(":common"))
    implementation(libs.spring.boot.mongo)

    implementation(libs.mapstruct.base)
    annotationProcessor(libs.mapstruct.processor)
    kapt(libs.mapstruct.processor)
}