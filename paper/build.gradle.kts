plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

dependencies {
    compileOnly(project(":common"))
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
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

