plugins {
    kotlin("jvm") version "2.4.21" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
