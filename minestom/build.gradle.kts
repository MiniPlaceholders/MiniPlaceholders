import java.time.LocalDate

plugins {
    id("miniplaceholders.shadow")
    id("miniplaceholders.auto.module")
    id("miniplaceholders.runtask")
    `java-library`
    id("com.vanniktech.maven.publish")
}

val shadowInclude by configurations.creating

configurations {
    compileOnly.get().extendsFrom(shadowInclude)
    testImplementation.get().extendsFrom(shadowInclude)
}

dependencies {
    compileOnly(libs.minestom)
    compileOnly(libs.adventure.api)
    annotationProcessor(libs.minestom)
    shadowInclude(projects.miniplaceholdersCommon)
    api(projects.miniplaceholdersApi)
    shadowInclude(projects.miniplaceholdersConnect)

    testImplementation(libs.minestom)
    testImplementation(libs.adventure.api)
    testImplementation(libs.adventure.minimessage)
    testImplementation("ch.qos.logback:logback-classic:1.5.32") // logger
    testImplementation("com.google.guava:guava:33.5.0-jre")
}

tasks {
    shadowJar {
        configurations = listOf(shadowInclude)
    }
    test {
        failOnNoDiscoveredTests = false
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(project.group as String, "miniplaceholders-minestom", project.version as String)

    pom {
        name.set(project.name)
        description.set(project.description)
        inceptionYear.set(LocalDate.now().year.toString())
        url.set("https://github.com/MiniPlaceholders/MiniPlaceholders")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("4drian3d")
                name.set("Adrian Gonzales")
                email.set("adriangonzalesval@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:https://github.com/MiniPlaceholders/MiniPlaceholders.git")
            developerConnection.set("scm:git:ssh://git@github.com/MiniPlaceholders/MiniPlaceholders.git")
            url.set("https://github.com/MiniPlaceholders/MiniPlaceholders")
        }
        ciManagement {
            name.set("GitHub Actions")
            url.set("https://github.com/MiniPlaceholders/MiniPlaceholders/actions")
        }
        issueManagement {
            name.set("GitHub")
            url.set("https://github.com/MiniPlaceholders/MiniPlaceholders/issues")
        }
    }

    configurations {
        named("runtimeElements") {
            outgoing {
                artifacts.clear()
                artifact(tasks.shadowJar)
            }
        }
        named("apiElements") {
            outgoing {
                artifacts.clear()
                artifact(tasks.shadowJar)
            }
        }
    }
}
