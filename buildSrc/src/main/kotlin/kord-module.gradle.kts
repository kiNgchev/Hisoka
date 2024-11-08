import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    org.jetbrains.kotlin.jvm
    org.jetbrains.kotlin.plugins.`kotlinx-serialization`
}

val libs = the<LibrariesForLibs>()

repositories {
    mavenCentral()
    maven("https://repo.kord.dev/snapshots")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(libs.bundles.kord)
}