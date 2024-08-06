plugins {
    kotlin("jvm") version "2.0.10" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
