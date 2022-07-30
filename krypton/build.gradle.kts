repositories {
    maven("https://repo.kryptonmc.org/releases")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly(project(":miniplaceholders-common"))
    compileOnly(project(":miniplaceholders-api"))
    compileOnly(project(":miniplaceholders-connect"))
    compileOnly("org.kryptonmc", "krypton-api", "0.66.1")
    annotationProcessor("org.kryptonmc", "krypton-annotation-processor", "0.66.1")
}