plugins {
    kotlin("jvm") version "1.9.0" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
