import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import java.time.LocalDate

plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka") version "2.1.0"
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimessage)
    compileOnly(projects.miniplaceholdersApi)
}

kotlin {
    explicitApi()
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
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

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(project.group as String, "miniplaceholders-kotlin-ext", project.version as String)
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
    configure(KotlinJvm(
        javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
        sourcesJar = SourcesJar.Sources(),
    ))
}


