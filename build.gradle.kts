plugins {
    kotlin("jvm") version "2.1.21" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
