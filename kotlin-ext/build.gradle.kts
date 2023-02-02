plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "1.7.20"
    `maven-publish`
}

dependencies {
    compileOnly(kotlin("stdlib", "1.8.10"))
    compileOnly("net.kyori:adventure-api:4.12.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
    compileOnly(projects.miniplaceholdersApi)
}

tasks {
    javadoc {
        enabled = false
    }

    build {
        dependsOn(dokkaHtmlJar)
    }
}

kotlin {
    explicitApi()
}

java {
    withJavadocJar()
    withSourcesJar()
}

// Credits to: https://github.com/Kotlin/dokka/issues/42#issuecomment-1055906110
/*val javadocJar = tasks.named<Jar>("javadocJar") {
    from(tasks.named("dokkaHtml"))
}*/

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = "miniplaceholders-kotlin-ext"
            version = project.version as String
            from(components["kotlin"])
        }
    }
}

val dokkaHtmlJar = tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html-docs")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
