plugins {
    kotlin("jvm") version "2.2.20" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
