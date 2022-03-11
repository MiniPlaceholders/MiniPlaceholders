plugins {
    id("me.champeau.jmh") version "0.6.6"
    `maven-publish`
}

dependencies {
    compileOnly("net.kyori:adventure-api:4.10.1")
    compileOnly("net.kyori:adventure-text-minimessage:4.10.1")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.10.1")
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("net.kyori:adventure-api:4.10.1")
    testImplementation("net.kyori:adventure-text-minimessage:4.10.1")
    testImplementation("net.kyori:adventure-text-serializer-plain:4.10.1")
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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

val projectGroup = project.group as String
val projectVersion = project.version as String

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = projectGroup
            artifactId = project.name
            version = projectVersion
            from(components["java"])
        }
    }
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).links(
        "https://jd.adventure.kyori.net/api/4.10.1/",
        "https://jd.adventure.kyori.net/text-minimessage/4.10.1/"
    )
}