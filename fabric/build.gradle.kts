plugins {
    id("miniplaceholders.auto.module")
    id("fabric-loom")
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
    includeDependency(libs.luckopermissionsapi)
    includeDependency(libs.cloud.fabric)
    includeDependency(libs.cloud.core)
    includeDependency(libs.cloud.extras)
    includeDependency(libs.desertwell)

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
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("MiniPlaceholders-Fabric-${project.version}.jar")
        destinationDirectory.set(file("${project.rootDir}/jar"))
    }
    shadowJar {
        configurations = listOf(shade)
    }
}

java {
    withSourcesJar()
}