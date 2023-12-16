plugins {
    id("twomartens.spring-boot-cloud-base")
    id("twomartens.kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(libs.spring.boot.actuator)
    implementation(libs.spring.boot.web)
    implementation(libs.spring.openapi)

    implementation(libs.bundles.spring.boot.security)
    implementation(libs.spring.cloud.leader.election)
    implementation(libs.spring.cloud.starter.bus.kafka)
}