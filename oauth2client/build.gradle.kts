plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(Constants.COMPILE_SDK_VERSION)

    defaultConfig {
        minSdkVersion(Constants.MIN_SDK_VERSION)
        targetSdkVersion(Constants.TARGET_SDK_VERSION)

        versionCode = Constants.VERSION_CODE
        versionName = Constants.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = true
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
    lintOptions {
        disable("ObsoleteLintCustomCheck")
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    // Kotlin
    implementation(Constants.KOTLIN_JDK_8)
    implementation(platform(Constants.COROUTINES_BOM))
    implementation(Constants.COROUTINES)
    implementation(Constants.COROUTINES_ANDROID)

    // Android
    implementation(Constants.ANDROID_KTX_CORE)

    // Core Libraries
    implementation(Constants.ANDROID_SECURITY)
    implementation(Constants.APP_AUTH)

    // Utils
    implementation(Constants.TIMBERKT)
}