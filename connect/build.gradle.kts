import java.time.LocalDate

plugins {
    id("miniplaceholders.base")
    id("com.vanniktech.maven.publish")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(project.group as String, "miniplaceholders-connect", project.version as String)

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
