plugins {
    java
    id("me.champeau.jmh") version "0.6.6"
}

dependencies {
    compileOnly("net.kyori:adventure-api:4.10.1")
    compileOnly("net.kyori:adventure-text-minimessage:4.10.1")
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:4.1.0")
    testImplementation("net.kyori:adventure-api:4.10.1")
    testImplementation("net.kyori:adventure-text-minimessage:4.10.1")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
		    events("passed", "skipped", "failed")
	    }
    }
}

jmh {
    warmupIterations.set(2)
    iterations.set(2)
    fork.set(2)
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11