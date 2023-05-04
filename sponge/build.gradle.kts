import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    id("org.spongepowered.gradle.plugin") version "2.1.1"
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.jpenilla.xyz/snapshots/")
}

dependencies {
    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)
    implementation(libs.cloud.sponge)
}

sponge {
    apiVersion("8.1.0")
    license("GPL-3")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("miniplaceholders") {
        displayName("MiniPlaceholders")
        entrypoint("io.github.miniplaceholders.sponge.SpongePlugin")
        description(project.description)
        links {
            homepage("https://github.com/MiniPlaceholders/MiniPlaceholders")
            source("https://github.com/MiniPlaceholders/MiniPlaceholders")
            issues("https://github.com/MiniPlaceholders/MiniPlaceholders/issues")
        }
        contributor("4drian3d") {
            description("Lead Developer")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
     jar {
        manifest {
            attributes("Automatic-Module-Name" to "io.github.miniplaceholders.sponge")
        }
    }
}

