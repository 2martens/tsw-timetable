plugins {
    id("twomartens.spring-boot-cloud")
    id("twomartens.kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":support"))
    implementation(libs.mapstruct.base)
    implementation(libs.bundles.spring.boot.security)
    implementation(libs.jaxb.impl)
    implementation(libs.jakarta.xml.binding)
    annotationProcessor(libs.mapstruct.processor)
    kapt(libs.mapstruct.processor)
    implementation(libs.spring.cloud.starter.config)
    implementation(libs.spring.cloud.leader.election)
    implementation(libs.spring.cloud.starter.bus.kafka)
}
