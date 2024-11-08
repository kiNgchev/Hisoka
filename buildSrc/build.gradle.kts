plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.plugins)
    implementation(files(libs::class.java.superclass.protectionDomain.codeSource.location))
}

kotlin {
    jvmToolchain(21)
}