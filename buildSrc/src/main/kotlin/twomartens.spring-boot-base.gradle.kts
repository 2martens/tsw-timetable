import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("org.springframework.boot")
    id("twomartens.java")
}

val libs = the<LibrariesForLibs>()

dependencies {
    implementation(platform(libs.spring.boot))
}