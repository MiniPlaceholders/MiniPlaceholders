plugins {
    kotlin("jvm")
    kotlin("kapt")
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
    compileOnly(projects.miniplaceholdersCommon)
    compileOnly(projects.miniplaceholdersApi)
    compileOnly(projects.miniplaceholdersConnect)
    compileOnly(projects.miniplaceholdersKotlinExt)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}