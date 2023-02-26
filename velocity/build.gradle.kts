plugins {
    alias(libs.plugins.runvelocity)
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
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

    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}
