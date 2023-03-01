plugins {
    kotlin("jvm")
    kotlin("kapt")
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.kryptonmc.org/releases")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.kryptonmc.org/snapshots")
}

dependencies {
    compileOnly(libs.krypton.server)
    compileOnly(kotlin("stdlib"))
    kapt(libs.krypton.annotation)
    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)
    implementation(projects.miniplaceholdersKotlinExt)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}