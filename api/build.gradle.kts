plugins {
    alias(libs.plugins.jmh)
    id("miniplaceholders.publish")

}

dependencies {
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimesssage)
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.13.0")
    compileOnly(projects.miniplaceholdersConnect)
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.adventure.api)
    testImplementation(libs.adventure.minimesssage)
    testImplementation("net.kyori:adventure-text-serializer-plain:4.13.0")
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
    withJavadocJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(11)
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
        (options as StandardJavadocDocletOptions).links(
            "https://jd.advntr.dev/api/${libs.versions.adventure.get()}/",
            "https://jd.advntr.dev/text-minimessage/${libs.versions.adventure.get()}/"
        )
    }
}
