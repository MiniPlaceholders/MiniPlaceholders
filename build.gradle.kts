plugins {
    kotlin("jvm") version "2.1.20" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
