plugins {
    kotlin("jvm") version "2.0.0" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
