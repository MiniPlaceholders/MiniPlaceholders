plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
}

val dokkaHtmlJar by tasks.registering(Jar::class) {
    description = "A HTML Documentation JAR containing Dokka HTML"
    from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

dokka {
    dokkaSourceSets.main {
        includes.from("README.md")
        sourceLink {
            localDirectory.set(rootDir)
            remoteUrl("https://github.com/MiniPlaceholders/MiniPlaceholders/tree/main")
            remoteLineSuffix.set("#L")
        }
    }
    pluginsConfiguration.html {
        footerMessage.set("(c) MiniPlaceholders | 4drian3d")
    }
}

val projectVersion = project.version.toString()
val projectName = project.name
val projectDescription = project.description
publishing {
    publications {
        register<MavenPublication>("maven") {
            artifact(dokkaHtmlJar)
            repositories {
                maven {
                    credentials {
                        username = findProperty("sonatypeTokenUsername") as? String ?: ""
                        password = findProperty("sonatypeTokenPassword") as? String ?: ""
                    }
                    val central = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                    val snapshots = "https://central.sonatype.com/repository/maven-snapshots/"


                    if (projectVersion.endsWith("SNAPSHOT") || projectVersion.contains("RC")) {
                        name = "SonatypeSnapshots"
                        setUrl(snapshots)
                    } else {
                        name = "OSSRH"
                        setUrl(central)
                    }
                }
                maven(file("build/dev-maven")) {
                    name = "DevMaven"
                }
            }
            from(components["java"])
            pom {
                url.set("https://github.com/MiniPlaceholders/MiniPlaceholders")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://opensource.org/license/apache-2-0/")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/MiniPlaceholders/MiniPlaceholders.git")
                    developerConnection.set("scm:git:ssh://git@github.com/MiniPlaceholders/MiniPlaceholders.git")
                    url.set("https://github.com/MiniPlaceholders/MiniPlaceholders")
                }
                developers {
                    developer {
                        id.set("4drian3d")
                        name.set("Adrian Gonzales")
                        email.set("adriangonzalesval@gmail.com")
                    }
                }
                issueManagement {
                    name.set("GitHub")
                    url.set("https://github.com/MiniPlaceholders/MiniPlaceholders/issues")
                }
                ciManagement {
                    name.set("GitHub Actions")
                    url.set("https://github.com/MiniPlaceholders/MiniPlaceholders/actions")
                }
                name.set(projectName)
                description.set(projectDescription)
                url.set("https://github.com/MiniPlaceholders/MiniPlaceholders")
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(configurations.archives.get())
    sign(publishing.publications["maven"])
    sign(dokkaHtmlJar.get())
}

java {
    withSourcesJar()
}