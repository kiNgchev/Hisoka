@file:Suppress("UnstableApiUsage")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "Hisoka"

include(":hisoka-common")
include("hisoka-worker")
// Оставил на прошлом компьютере :)
//include("hisoka-modules")
//include("hisoka-modules:hisoka-moderation")
//findProject(":hisoka-modules:hisoka-moderation")?.name = "hisoka-moderation"

include("hisoka-dsl")
include("hisoka-shared")