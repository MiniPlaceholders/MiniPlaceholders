plugins {
    alias(libs.plugins.runpaper)
    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(libs.paper)
    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)
    implementation(libs.cloud.paper)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    runServer {
        minecraftVersion("1.19.4")
    }
    jar {
        manifest {
            attributes("Automatic-Module-Name" to "io.github.miniplaceholders.paper")
        }
    }
    processResources {
        filesMatching("paper-plugin.yml") {
            expand("version" to project.version)
        }
    }
}

