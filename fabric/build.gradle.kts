import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.deleteIfExists

plugins {
    id("miniplaceholders.auto.module")
    id("quiet-fabric-loom")
    alias(libs.plugins.shadow)
}

val shade: Configuration by configurations.creating

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    includeDependency(libs.adventure.platform.fabric)
    includeDependency(libs.adventure.serializer.legacy)
    includeDependency(libs.adventure.minimessage)
    includeDependency(libs.luckopermissionsapi)
    includeDependency(libs.desertwell)
    includeDependency(libs.unnamedinject)

    shadeModule(projects.miniplaceholdersApi)
    shadeModule(projects.miniplaceholdersCommon)
    shadeModule(projects.miniplaceholdersConnect)
}

fun DependencyHandlerScope.shadeModule(module: ProjectDependency) {
    shade(module) {
        isTransitive = false
    }
    implementation(module) {
        isTransitive = false
    }
}

fun DependencyHandlerScope.includeDependency(dependency: Any) {
    modImplementation(dependency)
    include(dependency)
}

tasks {
    val projectVersion = project.version
    val rootDir = project.rootDir
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("fabric.mod.json") {
            expand("version" to projectVersion)
        }
    }
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("MiniPlaceholders-Fabric-${projectVersion}.jar")
        destinationDirectory.set(file("${rootDir}/jar"))
    }
    shadowJar {
        configurations = listOf(shade)
    }
    // I know it's exactly the same logic for all modules,
    // but for some reason in this module it only works
    // if it's declared in the same project configuration
    // and not with a plugin like with all other projects...
    // ¯\_(ツ)_/¯
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

java {
    withSourcesJar()
}