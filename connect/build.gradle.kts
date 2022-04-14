java.toolchain.languageVersion.set(JavaLanguageVersion.of(11))

tasks.compileJava {
    options.encoding = Charsets.UTF_8.name()

    options.release.set(11)
}