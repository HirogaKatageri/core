buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Constants.KOTLIN_VERSION}")
        classpath("com.google.gms:google-services:${Constants.GOOGLE_SERVICES_VERSION}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Constants.NAVIGATION_VERSION}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.5.0")
        classpath("io.insert-koin:koin-gradle-plugin:${Constants.KOIN_VERSION}")
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
