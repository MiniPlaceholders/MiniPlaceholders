plugins {
    kotlin("jvm") version "1.9.24" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
