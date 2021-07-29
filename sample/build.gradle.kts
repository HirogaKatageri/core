import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
}

val buildPropertiesFile = File("sample/build.properties")
val buildProperties = Properties()
if (buildPropertiesFile.exists()) buildProperties.load(FileInputStream(buildPropertiesFile))

android {
    compileSdk = Constants.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "dev.hirogakatageri.android.sandbox"
        minSdk = Constants.MIN_SDK_VERSION
        targetSdk = Constants.TARGET_SDK_VERSION

        versionCode = Constants.VERSION_CODE
        versionName = Constants.SAMPLE_VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "TWITCH_CLIENT_ID",
            buildProperties.getProperty("TWITCH_CLIENT_ID") ?: "null"
        )

        buildConfigField(
            "String",
            "TWITCH_SECRET_KEY",
            buildProperties.getProperty("TWITCH_SECRET_KEY") ?: "null"
        )
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":oauth2client"))
    implementation(project(":viewservice"))

    // Kotlin
    implementation(Constants.KOTLIN_JDK_8)

    implementation(platform(Constants.COROUTINES_BOM))

    implementation(Constants.COROUTINES)
    implementation(Constants.COROUTINES_ANDROID)
    testImplementation(Constants.COROUTINES_TEST)

    // Koin
    implementation(Constants.KOIN_ANDROID)
    implementation(Constants.KOIN_ANDROID_EXT)

    // Android
    implementation(Constants.APPCOMPAT)
    implementation(Constants.MATERIAL)
    implementation(Constants.CONSTRAINT_LAYOUT)

    // Android Kotlin Extensions
    implementation(Constants.ANDROID_KTX_CORE)
    implementation(Constants.ANDROID_KTX_ACTIVITY)
    implementation(Constants.ANDROID_KTX_FRAGMENT)

    // Android Lifecycle Libraries
    implementation(Constants.ANDROID_LIFECYCLE_SERVICE)

    // Android Jetpack Navigation
    implementation(Constants.NAVIGATION_FRAGMENT)
    implementation(Constants.NAVIGATION_UI)
    androidTestImplementation(Constants.NAVIGATION_TESTING)

    // Android Camera
    implementation(Constants.ANDROID_CAMERAX_CORE)
    implementation(Constants.ANDROID_CAMERAX_CAMERA2)
    implementation(Constants.ANDROID_CAMERAX_LIFECYCLE)
    implementation(Constants.ANDROID_CAMERAX_VIEW)

    // Core
    implementation(Constants.DATE_TIME)
    implementation(Constants.TIMBERKT)
    implementation(Constants.LEAK_CANARY)

    // Testing
    testImplementation(Constants.JUNIT)
    testImplementation(Constants.ANDROIDX_TEST)
    testImplementation(Constants.ANDROIDX_TEST_JUNIT)
    testImplementation(Constants.ROBOLECTRIC)
}