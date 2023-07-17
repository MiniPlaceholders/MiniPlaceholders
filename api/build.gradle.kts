plugins {
    id("miniplaceholders.base")
    alias(libs.plugins.jmh)
    id("miniplaceholders.publish")
}

dependencies {
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimesssage)
    compileOnly(libs.adventure.serializer.legacy)
    compileOnly(projects.miniplaceholdersConnect)
    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.adventure.api)
    testImplementation(libs.adventure.minimesssage)
    testImplementation(libs.adventure.serializer.plain)
    testImplementation(libs.adventure.serializer.legacy)
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
}

tasks {
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
    compileTestJava {
        options.encoding = Charsets.UTF_8.name()
    }
}
