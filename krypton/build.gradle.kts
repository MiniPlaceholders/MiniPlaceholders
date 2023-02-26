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
    compileOnly("org.kryptonmc:krypton-server:499879f4ef")
    compileOnly(kotlin("stdlib", "1.8.10"))
    kapt("org.kryptonmc:annotation-processor:0.66.2")
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