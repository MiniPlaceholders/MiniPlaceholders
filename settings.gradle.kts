enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "miniplaceholders-parent"

pluginManagement {
    @Suppress("UnstableApiUsage")
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

arrayOf(
    "connect",
    "api",
    "kotlin-ext",
    "common",
    "paper",
    "velocity",
    "krypton"
).forEach {
    include("miniplaceholders-$it")
    project(":miniplaceholders-$it").projectDir = file(it)
}
