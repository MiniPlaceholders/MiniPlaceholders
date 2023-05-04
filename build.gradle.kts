plugins {
    java
    alias(libs.plugins.shadow)
    kotlin("jvm") version "1.8.21" apply false
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

repositories {
    maven("https://repo.jpenilla.xyz/snapshots/")
}

dependencies {
    implementation(projects.miniplaceholdersVelocity)
    implementation(projects.miniplaceholdersPaper)
    implementation(projects.miniplaceholdersSponge)
    //implementation(projects.miniplaceholdersKrypton)
}

tasks {
    shadowJar {
        archiveFileName.set("MiniPlaceholders-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        relocate("net.william278.desertwell", "io.github.miniplaceholders.libs.desertwell")
        relocate("cloud.commandframework", "io.github.miniplaceholders.libs.cloud")
    }
    build {
        dependsOn(shadowJar)
    }
}
