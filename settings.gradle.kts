@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "miniplaceholders-parent"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.quiltmc.org/repository/release/")
        maven("https://repo.jpenilla.xyz/snapshots/")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://central.sonatype.com/repository/maven-snapshots/") {
            name = "sonatype-snapshots"
            mavenContent {
                snapshotsOnly()
            }
        }
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.maven.apache.org/maven2/")
        maven("https://repo.spongepowered.org/repository/")
        maven("https://jitpack.io") {
            mavenContent {
                includeGroup("net.william278")
            }
        }
        maven("https://repo.jpenilla.xyz/snapshots/")
    }
}

plugins {
    id("quiet-fabric-loom") version "1.15.331"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("com.vanniktech.maven.publish") version "0.36.0" apply false
    kotlin("jvm") version "2.3.10" apply false
}

arrayOf(
    "connect",
    "api",
    "kotlin-ext",
    "common",
    "paper",
    "velocity",
    "fabric",
    "sponge",
    "example-expansion-provider"
).forEach {
    include("miniplaceholders-$it")
    project(":miniplaceholders-$it").projectDir = file(it)
}
