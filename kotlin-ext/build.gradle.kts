plugins {
    kotlin("jvm")
    id("miniplaceholders.kotlin.publish")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimessage)
    compileOnlyApi(projects.miniplaceholdersApi)
}

kotlin {
    explicitApi()
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

