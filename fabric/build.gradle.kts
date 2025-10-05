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
}

java {
    withSourcesJar()
}