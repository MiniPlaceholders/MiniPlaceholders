plugins {
    id("miniplaceholders.auto.module")
    alias(libs.plugins.idea.ext)
    alias(libs.plugins.blossom)
}

dependencies {
    implementation(projects.miniplaceholdersApi)
    implementation(libs.desertwell) {
        exclude("org.json")
    }
    implementation(libs.unnamedinject)
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.jspecify)
    compileOnly(libs.brigadier)
    compileOnly(libs.bstats.core)
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
        }
    }
}
