plugins {
    kotlin("jvm") version "1.8.22" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
