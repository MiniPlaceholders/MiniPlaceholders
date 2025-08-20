plugins {
    kotlin("jvm") version "2.2.20-RC" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
