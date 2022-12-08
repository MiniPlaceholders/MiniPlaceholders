plugins {
    kotlin("jvm") version "1.7.21"
    kotlin("kapt") version "1.7.21"
}

repositories {
    maven("https://repo.kryptonmc.org/releases")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.kryptonmc.org/snapshots")
}

dependencies {
    compileOnly("org.kryptonmc:krypton-server:0.66.3")
    compileOnly(kotlin("stdlib", "1.7.21"))
    kapt("org.kryptonmc:annotation-processor:0.66.2")
    compileOnly(project(":miniplaceholders-common"))
    compileOnly(project(":miniplaceholders-api"))
    compileOnly(project(":miniplaceholders-connect"))
    compileOnly(project(":miniplaceholders-kotlin-ext"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}