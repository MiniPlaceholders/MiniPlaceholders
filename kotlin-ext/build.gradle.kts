plugins {
    kotlin("jvm") version "1.7.21"
    id("org.jetbrains.dokka") version "1.7.20"
    `maven-publish`
}

dependencies {
    compileOnly(kotlin("stdlib", "1.7.21"))
    compileOnly("net.kyori:adventure-api:4.12.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
    compileOnly(project(":miniplaceholders-api"))
}

tasks {
    javadoc {
        enabled = false
    }

    build {
        dependsOn(dokkaHtml)
    }

    dokkaHtml {
        outputDirectory.set(buildDir.resolve("docs").resolve("javadoc"))
    }
}

kotlin {
    explicitApi()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = "miniplaceholders-kotlin-ext"
            version = project.version as String
        }
    }
}
