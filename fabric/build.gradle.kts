import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.deleteIfExists

plugins {
    alias(libs.plugins.fabric.loom)
    id("miniplaceholders.auto.module")
}

repositories {
    maven("https://central.sonatype.com/repository/maven-snapshots/") {
        name = "sonatype-snapshots"
            mavenContent {
                snapshotsOnly()
            }
    }
    maven("https://jitpack.io")
}

val shade: Configuration by configurations.creating

dependencies {
    minecraft(libs.minecraft)
    compileOnly(libs.fabric.loader)
    compileOnly(libs.fabric.api)

    includeDependency(libs.adventure.platform.fabric)
    includeDependency(libs.adventure.serializer.legacy)
    includeDependency(libs.adventure.minimessage)
    includeDependency(libs.luckopermissionsapi)
    includeDependency(libs.unnamedinject)
    includeDependency(libs.desertwell)

    shadeModule(projects.miniplaceholdersApi)
    shadeModule(projects.miniplaceholdersCommon)
    shadeModule(projects.miniplaceholdersConnect)
}

fun DependencyHandlerScope.shadeModule(module: ProjectDependency) {
    implementation(module) {
        isTransitive = false
    }
    include(module) {
        isTransitive = false
    }
}

fun DependencyHandlerScope.includeDependency(dependency: Any) {
    implementation(dependency)
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
    jar {
        archiveFileName.set("MiniPlaceholders-Fabric-${projectVersion}.jar")
        destinationDirectory.set(file("${rootDir}/jar"))
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