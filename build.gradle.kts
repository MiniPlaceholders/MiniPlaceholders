plugins {
    java
    alias(libs.plugins.shadow)
    kotlin("jvm") version "1.8.20" apply false
}

allprojects {
    apply<JavaPlugin>()
    repositories {
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.maven.apache.org/maven2/")
        maven("https://jitpack.io") {
            mavenContent {
                includeGroup("net.william278")
            }
        }
    }
}

dependencies {
    implementation(projects.miniplaceholdersVelocity)
    implementation(project(":miniplaceholders-paper", "reobf"))
    implementation(projects.miniplaceholdersKrypton)
}

tasks {
    shadowJar {
        archiveFileName.set("MiniPlaceholders-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        relocate("net.william278.desertwell", "io.github.miniplaceholders.libs.desertwell")
    }
    build {
        dependsOn(shadowJar)
    }
}
