plugins {
    java
}

repositories {
    maven("https://libraries.minecraft.net");
}

dependencies {
    implementation(project(":api"))
    compileOnly("net.kyori:adventure-api:4.10.1")
    compileOnly("net.kyori:adventure-text-minimessage:4.10.1")
    compileOnly("com.mojang:brigadier:1.0.18")
}