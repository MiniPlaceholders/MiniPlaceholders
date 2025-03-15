plugins {
    id("miniplaceholders.auto.module")
    alias(libs.plugins.idea.ext)
    alias(libs.plugins.blossom)
}

version = "1.0.0"

dependencies {
    compileOnly(projects.miniplaceholdersApi)
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimessage)
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