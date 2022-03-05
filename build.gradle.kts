plugins {
    java
    id("me.champeau.jmh") version "0.6.6"
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.maven.apache.org/maven2/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")

    testImplementation("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:4.1.0")
}

group = "me.dreamerzero.miniplaceholders"
version = "1.0.0"
description = "MiniPlaceholders"
java.sourceCompatibility = JavaVersion.VERSION_11

tasks {
    test {
        useJUnitPlatform()
        testLogging {
		    events("passed", "skipped", "failed")
	    }
    }
}


tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

jmh {
    warmupIterations.set(2)
    iterations.set(2)
    fork.set(2)
}
