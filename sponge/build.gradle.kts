import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    id("miniplaceholders.auto.module")
    id("miniplaceholders.shadow")
    id("org.spongepowered.gradle.plugin")
}

dependencies {
    implementation(projects.miniplaceholdersCommon) {
        exclude(group = "org.incendo")
    }
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)
    //implementation(libs.cloud.sponge)
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

//tasks {
//    shadowJar {
//        exclude("io/leangen/geantyref/**")
//    }
//}
