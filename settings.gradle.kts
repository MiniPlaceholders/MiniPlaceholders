pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "miniplaceholders-parent"

include("miniplaceholders-api")
include("miniplaceholders-common")
include("miniplaceholders-paper")
include("miniplaceholders-velocity")

project(":miniplaceholders-api").projectDir = file("api")
project(":miniplaceholders-common").projectDir = file("common")
project(":miniplaceholders-velocity").projectDir = file("velocity")
project(":miniplaceholders-paper").projectDir = file("paper")
