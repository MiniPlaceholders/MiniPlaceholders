plugins {
    id("miniplaceholders.auto.module")
    id("miniplaceholders.shadow")
    id("miniplaceholders.runtask")
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
        jvmArgs("-Dcom.mojang.eula.agree=true")
        minecraftVersion(libs.versions.minecraft.get())
    }
    val projectVersion = project.version
    processResources {
        filesMatching("paper-plugin.yml") {
            expand("version" to projectVersion)
        }
    }
}

