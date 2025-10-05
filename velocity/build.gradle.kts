plugins {
    id("miniplaceholders.shadow")
    id("miniplaceholders.auto.module")
    id("miniplaceholders.runtask")
    alias(libs.plugins.runvelocity)
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)
}

tasks {
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}
