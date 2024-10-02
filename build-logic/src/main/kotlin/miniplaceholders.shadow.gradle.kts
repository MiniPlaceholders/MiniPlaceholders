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
        relocate("org.incendo.cloud", "io.github.miniplaceholders.libs.cloud")
        if (project.simpleName() != "sponge") {
            relocate("io.leangen.geantyref", "io.github.miniplaceholders.libs.geantyref")
        }
        destinationDirectory.set(file("${project.rootDir}/jar"))
    }
    build {
        dependsOn(shadowJar)
    }
}