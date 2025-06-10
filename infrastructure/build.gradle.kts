plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    kotlin("plugin.jpa")    version "1.9.25"
    kotlin("kapt") version "1.9.25"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(libs.kotlin.logging)
    implementation(libs.minio)
    implementation(libs.spring.boot.starter.data.jpa)
    runtimeOnly   (libs.postgresql)
    implementation("com.querydsl:querydsl-core:5.1.0")
    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
}