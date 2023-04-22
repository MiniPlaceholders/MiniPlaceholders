plugins {
    id("fabric-loom") version "1.1-SNAPSHOT"
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
    include(libs.adventure.platform.fabric)
    modImplementation(libs.luckopermissionsapi)
    include(libs.luckopermissionsapi)
    shadeModule(projects.miniplaceholdersCommon)
    shadeModule(projects.miniplaceholdersApi)
    shadeModule(projects.miniplaceholdersConnect)
}

fun DependencyHandlerScope.shadeModule(module: ProjectDependency) {
    shade(module)
    implementation(module)
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
        archiveFileName.set("MiniPlaceholders-Fabric-mc${libs.versions.minecraft.get()}-v${project.version}.jar")
    }
    shadowJar {
        configurations = listOf(shade)
    }
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}