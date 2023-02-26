plugins {
    id("net.kyori.blossom") version "1.3.1"
}

repositories {
    maven("https://libraries.minecraft.net") {
        mavenContent {
            includeGroup("com.mojang")
        }
    }
    mavenCentral()
}

dependencies {
    implementation(projects.miniplaceholdersApi)
    compileOnly("net.kyori:adventure-api:4.12.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
    compileOnly("com.mojang:brigadier:1.0.500")
}

blossom{
    replaceToken("{version}", project.version)
    replaceTokenIn("src/main/java/me/dreamerzero/miniplaceholders/common/PluginConstants.java")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    jar {
        manifest {
            attributes("Automatic-Module-Name" to "me.dreamerzero.miniplaceholders.common")
        }
    }
}
