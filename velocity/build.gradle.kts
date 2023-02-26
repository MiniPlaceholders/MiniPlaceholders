dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    compileOnly(projects.miniplaceholdersCommon)
    compileOnly(projects.miniplaceholdersApi)
    compileOnly(projects.miniplaceholdersConnect)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }

    jar {
        manifest {
            attributes("Automatic-Module-Name" to "me.dreamerzero.miniplaceholders.velocity")
        }
    }
}
