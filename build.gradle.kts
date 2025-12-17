plugins {
    kotlin("jvm") version "2.3.0" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
