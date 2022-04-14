dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    compileOnly(project(":miniplaceholders-common"))
    compileOnly(project(":miniplaceholders-api"))
    compileOnly(project(":miniplaceholders-connect"))
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks.compileJava {
    options.encoding = Charsets.UTF_8.name()

    options.release.set(17)
}