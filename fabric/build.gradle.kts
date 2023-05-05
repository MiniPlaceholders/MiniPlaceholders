plugins {
    id("fabric-loom") version "1.2.7"
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

val shade: Configuration by configurations.creating

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.adventure.platform.fabric)
    modImplementation(libs.cloud.fabric)
    include(libs.cloud.fabric)
    include(libs.adventure.platform.fabric)
    modImplementation(libs.luckopermissionsapi)
    include(libs.luckopermissionsapi)

    modImplementation(libs.cloud.core)
    include(libs.cloud.core)
    modImplementation(libs.cloud.extras)
    include(libs.cloud.extras)
    modImplementation(libs.desertwell)
    include(libs.desertwell)

    shadeModule(projects.miniplaceholdersCommon)
    shadeModule(projects.miniplaceholdersApi)
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

tasks {
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        manifest {
            attributes("Automatic-Module-Name" to "io.github.miniplaceholders.fabric")
        }
        archiveFileName.set("MiniPlaceholders-Fabric-${project.version}.jar")
    }
    shadowJar {
        configurations = listOf(shade)
    }
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}