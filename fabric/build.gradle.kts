plugins {
    id("fabric-loom") version "1.1-SNAPSHOT"
    alias(libs.plugins.shadow)
}

dependencies {
    minecraft(libs.minecraft)
    mappings("net.fabricmc:yarn:1.19.4+build.2:v2")
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.adventure.platform.fabric)

    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)

    include(libs.adventure.platform.fabric)
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
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}