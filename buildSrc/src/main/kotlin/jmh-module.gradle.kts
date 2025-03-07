import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("java")
    me.champeau.jmh
}

dependencies {
    implementation(libs.jmh.core)
    implementation(libs.jmh.generator.annprocess)
}