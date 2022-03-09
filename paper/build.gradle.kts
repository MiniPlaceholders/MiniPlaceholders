plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

dependencies {
    //TODO: Resolve Paper 1.18.2 dependency
    compileOnly("net.kyori:adventure-api:4.10.1")
    compileOnly("net.kyori:adventure-text-minimessage:4.10.1")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

bukkit {
    main = "me.dreamerzero.miniplaceholders.paper.PaperPlugin"
    apiVersion = "1.18"
    website = "https://github.com/4drian3d/MiniPlaceholders"
    authors = listOf("4drian3d")
    version = "1.0.0"
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11