import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    shadow(project(":api"))
    shadow(project(":paper"))
    shadow(project(":velocity"))
}

tasks {
    shadowJar {
        archiveFileName.set("MiniPlaceholders.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(project.configurations.shadow.get())
    }
    build {
        dependsOn(shadowJar)
    }
}