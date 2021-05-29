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
    compileSdkVersion(Constants.COMPILE_SDK_VERSION)

    defaultConfig {
        applicationId("dev.hirogakatageri.android.sandbox")
        minSdkVersion(Constants.MIN_SDK_VERSION)
        targetSdkVersion(Constants.TARGET_SDK_VERSION)

        versionCode = Constants.VERSION_CODE
        versionName = Constants.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "TWITCH_CLIENT_ID",
            buildProperties.getProperty("TWITCH_CLIENT_ID") ?: ""
        )

        buildConfigField(
            "String",
            "TWITCH_SECRET_KEY",
            buildProperties.getProperty("TWITCH_SECRET_KEY") ?: ""
        )
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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

    // Kotlin
    implementation(Constants.KOTLIN_JDK_8)

    implementation(platform(Constants.COROUTINES_BOM))

    implementation(Constants.COROUTINES)
    implementation(Constants.COROUTINES_ANDROID)
    testImplementation(Constants.COROUTINES_TEST)

    // Android
    implementation(Constants.APPCOMPAT)
    implementation(Constants.MATERIAL)
    implementation(Constants.CONSTRAINT_LAYOUT)

    implementation(Constants.ANDROID_KTX_CORE)
    implementation(Constants.ANDROID_KTX_ACTIVITY)
    implementation(Constants.ANDROID_KTX_FRAGMENT)

    implementation(Constants.NAVIGATION_FRAGMENT)
    implementation(Constants.NAVIGATION_UI)
    androidTestImplementation(Constants.NAVIGATION_TESTING)

    // Core
    implementation(Constants.DATE_TIME)
    implementation(Constants.TIMBERKT)
    implementation(Constants.LEAK_CANARY)

    // Security
    implementation(Constants.ANDROID_SECURITY)

    // Authentication
    implementation(Constants.APP_AUTH)

    // Testing
    testImplementation(Constants.JUNIT)
    testImplementation(Constants.ANDROIDX_TEST)
    testImplementation(Constants.ANDROIDX_TEST_JUNIT)
    testImplementation(Constants.ROBOLECTRIC)
}