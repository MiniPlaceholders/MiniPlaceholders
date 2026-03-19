plugins {
    kotlin("jvm") version "2.3.20" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
