import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val ROOT_PROJECT_VERSION = "0.0.1-SNAPSHOT"
    const val SPRING_DOC_VERSION = "2.1.0"
    const val JJWT_API_VERSION = "0.12.3"
    const val JVM_VERSION = "17"
    const val MOCKITO_VERSION = "2.2.0"
}

plugins {
    kotlin("jvm") version "1.9.23"             // Kotlin JVM
    kotlin("plugin.spring") version "1.9.22"   // Kotlin Spring
    kotlin("plugin.jpa") version "1.9.22"      // Kotlin JPA
    id("org.springframework.boot") version "3.2.4"  // Spring Boot
    id("io.spring.dependency-management") version "1.1.4" // Dependency management
    id("java")                                 // Java support
}

group = "ru.itech"
version = Versions.ROOT_PROJECT_VERSION

java {
    sourceCompatibility = JavaVersion.VERSION_17   // Java 17 compatibility
    targetCompatibility = JavaVersion.VERSION_17   // Target Java 17
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

extra["springCloudVersion"] = "2023.0.0"

dependencies {
    Versions.apply {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-web")

        // JWT dependencies
        implementation("io.jsonwebtoken:jjwt-api:$JJWT_API_VERSION")
        implementation("io.jsonwebtoken:jjwt-impl:$JJWT_API_VERSION")
        implementation("io.jsonwebtoken:jjwt-jackson:$JJWT_API_VERSION")

        // Jackson for Kotlin
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        // Kotlin dependencies
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        // OpenAPI (Swagger) dependencies
        implementation("org.springdoc:springdoc-openapi-starter-common:$SPRING_DOC_VERSION")
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$SPRING_DOC_VERSION")

        // Database dependencies
        implementation("org.postgresql:postgresql:42.2.8")

        // Spring Cloud for config server and Eureka
        implementation("org.springframework.cloud:spring-cloud-config-server")
        implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

        // Security
        implementation("org.springframework.boot:spring-boot-starter-security")

        // Test dependencies
        testImplementation("org.springframework.boot:spring-boot-starter-test")

        testImplementation("org.springframework.security:spring-security-test")
        testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:$MOCKITO_VERSION")
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

// Настройка компиляции Kotlin
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")  // Настройки для строгой проверки типов
        jvmTarget = Versions.JVM_VERSION               // Версия JVM для Kotlin
    }
}

// Настройка компиляции Java
tasks.withType<JavaCompile> {
    sourceCompatibility = Versions.JVM_VERSION       // Версия JVM для Java
    targetCompatibility = Versions.JVM_VERSION       // Версия JVM для Java
}

// Настройка тестов
tasks.withType<Test> {
    useJUnitPlatform()                               // Используем JUnit 5 (JUnit Platform)
}
