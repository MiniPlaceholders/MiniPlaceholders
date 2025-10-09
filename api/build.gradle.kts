import java.time.LocalDate

plugins {
    id("miniplaceholders.base")
    alias(libs.plugins.jmh)
    `java-library`
    id("com.vanniktech.maven.publish")
}

dependencies {
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.adventure.serializer.legacy)
    compileOnly(projects.miniplaceholdersConnect)
    compileOnlyApi(libs.unnamedinject)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.adventure.api)
    testImplementation(libs.adventure.minimessage)
    testImplementation(libs.adventure.serializer.plain)
    testImplementation(libs.adventure.serializer.legacy)

    compileOnly(libs.jspecify)
}

jmh {
    warmupIterations.set(2)
    iterations.set(2)
    fork.set(2)
}

tasks {
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    javadoc {
        val o = options as StandardJavadocDocletOptions
        o.encoding = Charsets.UTF_8.name()
        o.links(
            "https://jd.advntr.dev/api/${libs.versions.adventure.get()}/",
            "https://jd.advntr.dev/text-minimessage/${libs.versions.adventure.get()}/",
            "https://javadoc.io/doc/org.jspecify/jspecify/latest/index.html"
        )
        o.tags(
            "apiNote:a:API Note:",
            "implSpec:a:Implementation Requirements:",
            "implNote:a:Implementation Note:",
            "sinceMinecraft:a:Since Minecraft:"
        )
    }
    compileTestJava {
        options.encoding = Charsets.UTF_8.name()
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "failed")
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(project.group as String, "miniplaceholders-api", project.version as String)

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
}
