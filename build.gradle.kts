plugins {
    kotlin("jvm") version "1.8.21" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
