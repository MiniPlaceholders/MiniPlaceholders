plugins {
    kotlin("jvm") version "2.4.20" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
