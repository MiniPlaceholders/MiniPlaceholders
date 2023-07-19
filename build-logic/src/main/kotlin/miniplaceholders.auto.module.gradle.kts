plugins {
    id("miniplaceholders.base")
}

tasks {
    jar {
        manifest {
            attributes("Automatic-Module-Name" to "io.github.miniplaceholders.${project.simpleName()}")
        }
    }
}