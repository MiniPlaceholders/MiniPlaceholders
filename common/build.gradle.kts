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
    implementation(libs.cloud.core)
    implementation(libs.cloud.extras) {
        isTransitive = false
    }
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
