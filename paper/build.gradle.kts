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
    implementation(libs.cloud.paper)
}

tasks {
    runServer {
        minecraftVersion(libs.versions.minecraft.get())
    }
    processResources {
        filesMatching("paper-plugin.yml") {
            expand("version" to project.version)
        }
    }
}

