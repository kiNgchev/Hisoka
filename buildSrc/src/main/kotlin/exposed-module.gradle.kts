import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    org.jetbrains.kotlin.jvm
    org.jetbrains.kotlin.plugins.`kotlinx-serialization`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.exposed)
    implementation(libs.postgresql)
}