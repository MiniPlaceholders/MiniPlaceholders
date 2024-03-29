plugins {
    id("miniplaceholders.shadow")
    id("miniplaceholders.auto.module")
    alias(libs.plugins.runvelocity)
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
    implementation(projects.miniplaceholdersCommon)
    implementation(projects.miniplaceholdersApi)
    implementation(projects.miniplaceholdersConnect)
    implementation(libs.cloud.velocity)
}

tasks {
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}
