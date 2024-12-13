import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    org.jetbrains.kotlin.jvm
    org.jetbrains.kotlin.plugins.`kotlinx-serialization`
}

val libs = the<LibrariesForLibs>()

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.slf4j.api)
    implementation(libs.logback.core)
    implementation(libs.dotenv)
    testImplementation(kotlin("test"))
}

kotlin {
    tasks.test {
        useJUnitPlatform()
    }

    explicitApi()

    jvmToolchain(21)

    compilerOptions {
        allWarningsAsErrors = true
    }
}