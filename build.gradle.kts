plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.8.0" apply false
}

allprojects {
    apply<JavaPlugin>()
}

dependencies {
    shadow(projects.miniplaceholdersConnect)
    shadow(projects.miniplaceholdersApi)
    shadow(projects.miniplaceholdersKotlinExt)
    shadow(projects.miniplaceholdersCommon)
    shadow(projects.miniplaceholdersVelocity)
    shadow(project(":miniplaceholders-paper", "reobf"))
    shadow(projects.miniplaceholdersKrypton)
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
        archiveFileName.set("MiniPlaceholders-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(project.configurations.shadow.get())
    }
    build {
        dependsOn(shadowJar)
    }
}
