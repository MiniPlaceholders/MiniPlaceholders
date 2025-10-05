import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.deleteIfExists

plugins {
    id("miniplaceholders.shadow")
    id("miniplaceholders.auto.module")
    alias(libs.plugins.runvelocity)
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)
}

tasks {
    runVelocity {
        val exampleExpansionProviderProject = project(":miniplaceholders-example-expansion-provider")
        val exampleExpansionJarTask = exampleExpansionProviderProject.tasks.named<Jar>("jar")
        dependsOn(exampleExpansionJarTask)
        doFirst {
            val expansionsDirectory = runDirectory.get().file("plugins/miniplaceholders/expansions").asFile.toPath()
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
        velocityVersion(libs.versions.velocity.get())
    }
}
