@file:Suppress("UnstableApiUsage")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "Hisoka"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":hisoka-common")
include("hisoka-worker")
