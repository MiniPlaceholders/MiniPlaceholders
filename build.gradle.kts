import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

allprojects {
    apply<JavaPlugin>()
}

dependencies {
    shadow(project(":miniplaceholders-connect"))
    shadow(project(":miniplaceholders-api"))
    shadow(project(":miniplaceholders-common"))
    shadow(project(":miniplaceholders-velocity"))
    shadow(project(":miniplaceholders-paper", "reobf"))
    shadow(project(":miniplaceholders-krypton"))
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.maven.apache.org/maven2/")
    }
}

tasks {
    shadowJar {
        archiveFileName.set("MiniPlaceholders-$version.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(project.configurations.shadow.get())
    }
    build {
        dependsOn(shadowJar)
    }
}
