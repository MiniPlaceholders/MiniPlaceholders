plugins {
    java
    alias(libs.plugins.shadow)
    kotlin("jvm") version "1.8.21" apply false
}

allprojects {
    apply<JavaPlugin>()
}

dependencies {
    implementation(projects.miniplaceholdersVelocity)
    implementation(project(":miniplaceholders-paper", "reobf"))
    implementation(projects.miniplaceholdersKrypton)
}

subprojects {
    repositories {
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.maven.apache.org/maven2/")
    }
}

tasks {
    shadowJar {
        archiveFileName.set("MiniPlaceholders-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    build {
        dependsOn(shadowJar)
    }
}
