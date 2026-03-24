plugins {
    id("miniplaceholders.shadow")
    id("miniplaceholders.auto.module")
    id("miniplaceholders.runtask")
}

dependencies {
    compileOnly(libs.minestom)
    compileOnly(libs.adventure.api)
    annotationProcessor(libs.minestom)
    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)

    testImplementation(libs.minestom)
    testImplementation(libs.adventure.api)
    testImplementation(libs.adventure.minimessage)
    testImplementation("ch.qos.logback:logback-classic:1.5.32") // logger
    testImplementation("com.google.guava:guava:33.5.0-jre")
}

tasks.test {
    failOnNoDiscoveredTests = false
}
