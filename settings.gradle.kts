enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "miniplaceholders-parent"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.quiltmc.org/repository/release/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

arrayOf(
    "connect",
    "api",
    "kotlin-ext",
    "common",
    "paper",
    "velocity",
    "krypton",
    "fabric"
).forEach {
    include("miniplaceholders-$it")
    project(":miniplaceholders-$it").projectDir = file(it)
}
