plugins {
    kotlin("jvm") version "1.9.10" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
