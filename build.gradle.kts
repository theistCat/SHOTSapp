buildscript {
    val compose_version by extra("1.2.0-rc01")
    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}



tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=org.mylibrary.OptInAnnotation"
}