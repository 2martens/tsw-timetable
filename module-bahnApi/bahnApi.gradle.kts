plugins {
    id("twomartens.spring-boot-cloud-base")
    id("twomartens.kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":model"))
    implementation(project(":support"))

    implementation(libs.mapstruct.base)
    annotationProcessor(libs.mapstruct.processor)
    kapt(libs.mapstruct.processor)

    implementation(libs.jaxb.impl)
    implementation(libs.jakarta.xml.binding)

    implementation(libs.spring.openapi)
    implementation(libs.spring.boot.mongo)
    implementation(libs.spring.cloud.leader.election)
    implementation(libs.spring.cloud.starter.bus.kafka)
}