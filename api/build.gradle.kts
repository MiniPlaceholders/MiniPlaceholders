plugins {
    id("me.champeau.jmh") version "0.6.6"
    `maven-publish`
}

dependencies {
    compileOnly("net.kyori:adventure-api:4.12.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.12.0")
    compileOnly(project(":miniplaceholders-connect"))
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = "miniplaceholders-api"
            version = project.version as String
            from(components["java"])
        }
    }
}

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
