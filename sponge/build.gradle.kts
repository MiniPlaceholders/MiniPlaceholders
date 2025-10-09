import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.deleteIfExists

plugins {
    id("miniplaceholders.auto.module")
    id("miniplaceholders.shadow")
    id("org.spongepowered.gradle.plugin") version "2.3.0"
}

dependencies {
    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)

    compileOnly(libs.jspecify)
    implementation(libs.bstats.sponge)
}

sponge {
    injectRepositories(false)
    apiVersion("12.0.0")
    license("GPL-3")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("miniplaceholders") {
        displayName("MiniPlaceholders")
        entrypoint("io.github.miniplaceholders.sponge.SpongePlugin")
        description(project.description)
        links {
            homepage("https://github.com/MiniPlaceholders/MiniPlaceholders")
            source("https://github.com/MiniPlaceholders/MiniPlaceholders")
            issues("https://github.com/MiniPlaceholders/MiniPlaceholders/issues")
        }
        contributor("4drian3d") {
            description("Lead Developer")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}

tasks {
    runServer {
        val exampleExpansionProviderProject = project(":miniplaceholders-example-expansion-provider")
        val exampleExpansionJarTask = exampleExpansionProviderProject.tasks.named<Jar>("jar")
        dependsOn(exampleExpansionJarTask)
        doFirst {
            val expansionsDirectory = projectDir.resolve("run/config/miniplaceholders/expansions").toPath()
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
    }
}
