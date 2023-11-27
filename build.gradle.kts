plugins {
    kotlin("jvm") version "1.9.21" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
