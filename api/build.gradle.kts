plugins {
    id("miniplaceholders.base")
    alias(libs.plugins.jmh)
    id("miniplaceholders.publish")
}

dependencies {
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.adventure.serializer.legacy)
    compileOnly(projects.miniplaceholdersConnect)

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

java {
    withJavadocJar()
}

tasks {
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
        val o = options as StandardJavadocDocletOptions
        o.links(
            "https://jd.advntr.dev/api/${libs.versions.adventure.get()}/",
            "https://jd.advntr.dev/text-minimessage/${libs.versions.adventure.get()}/"
        )
        o.tags(
            "apiNote:a:API Note:",
            "implSpec:a:Implementation Requirements:",
            "implNote:a:Implementation Note:"
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
