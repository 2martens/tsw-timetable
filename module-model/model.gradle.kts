plugins {
    id("twomartens.spring-boot-base")
    id("twomartens.kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(project(":common"))
    implementation(libs.spring.boot.mongo)
}