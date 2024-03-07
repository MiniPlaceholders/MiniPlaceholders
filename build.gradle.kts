plugins {
    kotlin("jvm") version "1.9.23" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
