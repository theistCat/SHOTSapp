buildscript {
    val compose_version by extra("1.3.0")
    dependencies {
        classpath("com.google.gms:google-services:4.3.14")
//        classpath("com.google.relay:relay-gradle-plugin:0.3.00")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.3.1" apply false
    id("com.android.library") version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
//    id("com.google.relay") version "0.3.00"
}



tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
//tasks.withType().configureEach {
//    kotlinOptions.freeCompilerArgs += "-opt-in=org.mylibrary.OptInAnnotation"
//}