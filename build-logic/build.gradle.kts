plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugin.shadow)
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.dokka)
}

repositories {
    gradlePluginPortal()
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}