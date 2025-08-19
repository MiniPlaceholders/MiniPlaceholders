plugins {
    kotlin("jvm") version "2.2.20-Beta2" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
