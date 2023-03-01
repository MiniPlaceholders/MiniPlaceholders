plugins {
    alias(libs.plugins.userdev)
    alias(libs.plugins.runpaper)
    alias(libs.plugins.shadow)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.get())
    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }
    runServer {
        minecraftVersion("1.19.3")
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

