plugins {
    org.jetbrains.kotlin.jvm
    org.jetbrains.kotlin.plugins.`kotlinx-serialization`
}

val libs = the<VersionCatalogsExtension>().named("libs")

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.findLibrary("kotlinx-serialization-json").get())
    implementation(libs.findLibrary("slf4j-api").get())
    implementation(libs.findLibrary("logback-core").get())
    implementation(libs.findLibrary("dotenv").get())
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