plugins {
    id("miniplaceholders.base")
    id("com.github.johnrengelman.shadow")
}

tasks {
    shadowJar {
        archiveBaseName.set("MiniPlaceholders-${project.capitalizeName}")
        archiveClassifier.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        relocate("net.william278.desertwell", "io.github.miniplaceholders.libs.desertwell")
        relocate("cloud.commandframework", "io.github.miniplaceholders.libs.cloud")
        relocate("io.leangen.geantyref", "io.github.miniplaceholders.libs.geantyref")
        destinationDirectory.set(file("${project.rootDir}/jar"))
    }
    build {
        dependsOn(shadowJar)
    }
}