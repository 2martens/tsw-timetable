plugins {
    id("twomartens.spring-boot-cloud-application")
    id("twomartens.kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(project(":bahnApi"))
    implementation(project(":common"))
    implementation(project(":model"))
    implementation(project(":support"))

    implementation(libs.jaxb.impl)
    implementation(libs.jakarta.xml.binding)

    implementation(libs.mapstruct.base)
    annotationProcessor(libs.mapstruct.processor)
    kapt(libs.mapstruct.processor)

    implementation(libs.spring.boot.batch)
    implementation(libs.bundles.spring.boot.security)
}
