plugins {
    kotlin("jvm") version "2.3.10" apply false
}

tasks {
    delete {
        delete("jar")
    }
}
