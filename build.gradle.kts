plugins {
    kotlin("jvm") version "2.1.0" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
