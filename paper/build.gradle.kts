plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    id("io.papermc.paperweight.userdev") version "1.5.1"
    id("xyz.jpenilla.run-paper") version "1.1.0"
}

dependencies {
    compileOnly(projects.miniplaceholdersCommon)
    compileOnly(projects.miniplaceholdersApi)
    compileOnly(projects.miniplaceholdersConnect)
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")
}

bukkit {
    name = "MiniPlaceholders"
    main = "me.dreamerzero.miniplaceholders.paper.PaperPlugin"
    apiVersion = "1.18"
    website = "https://github.com/4drian3d/MiniPlaceholders"
    authors = listOf("4drian3d")
    version = project.version as String
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }
    runServer {
        minecraftVersion("1.19.3")
    }
    jar {
        manifest {
            attributes("Automatic-Module-Name" to "me.dreamerzero.miniplaceholders.paper")
        }
    }
}

