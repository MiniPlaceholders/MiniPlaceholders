plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("io.papermc.paperweight.userdev") version "1.3.5"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

dependencies {
    compileOnly(project(":miniplaceholders-common"))
    compileOnly(project(":miniplaceholders-api"))
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")
    
    //compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    //compileOnly("io.papermc.paper:paper-mojangapi:1.18.2-R0.1-SNAPSHOT")
}

val pluginVersion = version

bukkit {
    main = "me.dreamerzero.miniplaceholders.paper.PaperPlugin"
    apiVersion = "1.18"
    website = "https://github.com/4drian3d/MiniPlaceholders"
    authors = listOf("4drian3d")
    version = pluginVersion as String
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks {

    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }
}

