plugins {
    alias(libs.plugins.runvelocity)
    alias(libs.plugins.shadow)
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }

    jar {
        manifest {
            attributes("Automatic-Module-Name" to "io.github.miniplaceholders.velocity")
        }
    }

    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}
