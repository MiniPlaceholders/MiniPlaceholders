plugins {
    id("miniplaceholders.auto.module")
    id("miniplaceholders.shadow")
    alias(libs.plugins.runpaper)
}

dependencies {
    compileOnly(libs.paper)
    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)
}

tasks {
    runServer {
        minecraftVersion(libs.versions.minecraft.get())
    }
    val projectVersion = project.version
    processResources {
        //val version = project.version
        filesMatching("paper-plugin.yml") {
            expand("version" to projectVersion)
        }
    }
}

