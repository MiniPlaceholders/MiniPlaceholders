plugins {
    kotlin("jvm") version "1.9.20" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
