plugins {
    kotlin("jvm")
    id("miniplaceholders.kotlin.publish")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimesssage)
    compileOnlyApi(projects.miniplaceholdersApi)
}

kotlin {
    explicitApi()
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

