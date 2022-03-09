plugins {
    java
}

allprojects {
    group = "me.dreamerzero.miniplaceholders"
    version = "1.0.0"
    description = "MiniPlaceholders"

    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.maven.apache.org/maven2/")
    }
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11
