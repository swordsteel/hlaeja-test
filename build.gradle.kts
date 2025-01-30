plugins {
    alias(hlaeja.plugins.kotlin.jvm)
    alias(hlaeja.plugins.kotlin.spring)
    alias(hlaeja.plugins.ltd.hlaeja.plugin.library)
    alias(hlaeja.plugins.spring.dependency.management)
    alias(hlaeja.plugins.springframework.boot)
}

dependencies {
    implementation(hlaeja.kotlinx.coroutines)
    implementation(hlaeja.springboot.starter.r2dbc)
    implementation(hlaeja.springboot.starter.test)
    implementation(hlaeja.testcontainers.junit)
    implementation(hlaeja.testcontainers.postgresql)

    testRuntimeOnly(hlaeja.junit.platform.launcher)
}

group = "ltd.hlaeja.library"
