import java.util.Date

plugins {
    java
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    jar {
        manifest {
            attributes(
                    "Specification-Version" to project.version,
                    "Specification-Vendor" to "MiniPlaceholders",
                    "Implementation-Build-Date" to Date()
            )
        }
    }
}

java{
    toolchain{
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}