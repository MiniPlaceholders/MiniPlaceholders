plugins {
    kotlin("jvm") version "2.1.10" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
