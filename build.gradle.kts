plugins {
    kotlin("jvm") version "2.0.21" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
