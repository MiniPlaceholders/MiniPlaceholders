pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "miniplaceholders-parent"

val modules = listOf("connect", "api", "common", "paper", "velocity", "krypton")

modules.forEach {
    include("miniplaceholders-$it")
    project(":miniplaceholders-$it").projectDir = file(it)
}

