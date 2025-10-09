plugins {
    id("miniplaceholders.base")
    id("com.gradleup.shadow")
}

tasks {
    shadowJar {
        archiveBaseName.set("MiniPlaceholders-${project.capitalizeName}")
        archiveClassifier.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        relocate("net.william278.desertwell", "io.github.miniplaceholders.libs.desertwell")
        relocate("org.bstats", "io.github.miniplaceholders.libs.bstats")

        destinationDirectory.set(file("${project.rootDir}/jar"))
    }
    build {
        dependsOn(shadowJar)
    }
}