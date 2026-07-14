plugins {
    kotlin("jvm") version "2.4.10" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
