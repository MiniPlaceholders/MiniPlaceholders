plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("kapt") version "1.6.21"
}

repositories {
    maven("https://repo.kryptonmc.org/releases")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.kryptonmc", "krypton-api", "0.66.1")
    kapt("org.kryptonmc", "api", "0.66.1")
    compileOnly(kotlin("stdlib"))
    //compileOnly("org.kryptonmc", "krypton-server", "0.66.1")
    compileOnly(project(":miniplaceholders-common"))
    compileOnly(project(":miniplaceholders-api"))
    compileOnly(project(":miniplaceholders-connect"))
}