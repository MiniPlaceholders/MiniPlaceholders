import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.deleteIfExists

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
        val exampleExpansionProviderProject = project(":miniplaceholders-example-expansion-provider")
        val exampleExpansionJarTask = exampleExpansionProviderProject.tasks.named<Jar>("jar")
        dependsOn(exampleExpansionJarTask)
        doFirst {
            val expansionsDirectory = runDirectory.get().file("plugins/MiniPlaceholders/expansions").asFile.toPath()
            if (Files.exists(expansionsDirectory)) {
                Files.newDirectoryStream(expansionsDirectory).use {
                    it.forEach { file ->
                        file.deleteIfExists()
                    }
                }
            } else {
                Files.createDirectories(expansionsDirectory)
            }
            val exampleExpansionFile = exampleExpansionJarTask.get().archiveFile.get().asFile.toPath()
            Files.copy(
                exampleExpansionFile,
                expansionsDirectory.resolve(exampleExpansionFile.fileName),
                StandardCopyOption.REPLACE_EXISTING
            )
        }
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

