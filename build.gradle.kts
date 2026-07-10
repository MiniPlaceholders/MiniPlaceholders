plugins {
    kotlin("jvm") version "2.4.0" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
