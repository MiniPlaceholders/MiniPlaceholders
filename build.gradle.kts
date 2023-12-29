plugins {
    kotlin("jvm") version "1.9.22" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
