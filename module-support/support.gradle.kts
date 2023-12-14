plugins {
    id("twomartens.spring-boot-cloud")
    id("twomartens.kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(libs.bundles.spring.boot.security)
    implementation(libs.spring.cloud.leader.election)
    implementation(libs.spring.cloud.starter.bus.kafka)
}