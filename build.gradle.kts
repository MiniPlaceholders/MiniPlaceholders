plugins {
    kotlin("jvm") version "2.2.21" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
