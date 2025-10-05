import org.gradle.api.tasks.bundling.Jar
import xyz.jpenilla.runtask.task.RunWithPlugins
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.deleteIfExists

tasks {
    withType<RunWithPlugins> {
        val exampleExpansionProviderProject = project(":miniplaceholders-example-expansion-provider")
        val exampleExpansionJarTask = exampleExpansionProviderProject.tasks.named<Jar>("jar")
        dependsOn(exampleExpansionJarTask)
        doFirst {
            val folderName = if (project.name.contains("velocity")) "miniplaceholders" else "MiniPlaceholders"
            val expansionsDirectory = runDirectory.get().file("plugins/$folderName/expansions").asFile.toPath()
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