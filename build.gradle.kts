buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Constants.KOTLIN_VERSION}")
        classpath("com.google.gms:google-services:${Constants.GOOGLE_SERVICES_VERSION}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Constants.NAVIGATION_VERSION}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
        classpath("io.insert-koin:koin-gradle-plugin:${Constants.KOIN_VERSION}")
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"

}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
