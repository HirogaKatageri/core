buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Constants.KOTLIN_VERSION}")
        classpath("com.google.gms:google-services:${Constants.GOOGLE_SERVICES_VERSION}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Constants.NAVIGATION_VERSION}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.32")
        classpath("io.insert-koin:koin-gradle-plugin:${Constants.KOIN_VERSION}")
    }
}

apply(plugin = "koin")

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/hirogakatageri/core")
            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
