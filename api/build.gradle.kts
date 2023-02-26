plugins {
    id("me.champeau.jmh") version "0.6.6"
    `maven-publish`
    signing
}

dependencies {
    compileOnly("net.kyori:adventure-api:4.12.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.12.0")
    compileOnly(projects.miniplaceholdersConnect)
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("net.kyori:adventure-api:4.12.0")
    testImplementation("net.kyori:adventure-text-minimessage:4.12.0")
    testImplementation("net.kyori:adventure-text-serializer-plain:4.12.0")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "failed")
        }
    }
}

jmh {
    warmupIterations.set(2)
    iterations.set(2)
    fork.set(2)
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}
/*
publishing {
    publications {
        create<MavenPublication>("maven") {
            repositories {
                maven {
                    credentials(PasswordCredentials::class)
                    val central = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                    val snapshots = "https://s01.oss.sonatype.org/content/repositories/snapshots/"

                    if (project.version.toString().endsWith("SNAPSHOT")) {
                        name = "SonatypeSnapshots"
                        setUrl(snapshots)
                    } else {
                        name = "OSSRH"
                        setUrl(central)
                    }
                }
            }
            from(components["java"])
            pom {
                url.set("https://github.com/MiniPlaceholders/MiniPlaceholders")
                licenses {
                    license {
                        name.set("GNU General Public License version 3 or later")
                        url.set("https://opensource.org/licenses/GPL-3.0")
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
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/MiniPlaceholders/MiniPlaceholders")
            }
        }
    }
}*/

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(11)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
        (options as StandardJavadocDocletOptions).links(
            "https://jd.adventure.kyori.net/api/4.10.0/",
            "https://jd.adventure.kyori.net/text-minimessage/4.11.0/"
        )
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}
/*
signing {
    useGpgCmd()
    sign(configurations.archives.get())
    sign(publishing.publications["maven"])
}*/
