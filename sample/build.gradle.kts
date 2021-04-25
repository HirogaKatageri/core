plugins {
    id("com.android.application")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Constants.COMPILE_SDK_VERSION)

    defaultConfig {
        applicationId("com.hirogakatageri.core.sample")
        minSdkVersion(Constants.MIN_SDK_VERSION)
        targetSdkVersion(Constants.TARGET_SDK_VERSION)

        versionCode = Constants.VERSION_CODE
        versionName = Constants.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.prog"
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

    implementation("com.hirogakatageri:core:0.1.2")

    implementation(Constants.KOTLIN_JDK_8)

    implementation(Constants.ANDROID_KTX_CORE)
    implementation(Constants.ANDROID_KTX_FRAGMENT)
    implementation(Constants.APPCOMPAT)
    implementation(Constants.MATERIAL)
    implementation(Constants.CONSTRAINT_LAYOUT)

    implementation(Constants.NAVIGATION_FRAGMENT)
    implementation(Constants.NAVIGATION_UI)
    androidTestImplementation(Constants.NAVIGATION_TESTING)

    implementation(Constants.DATE_TIME)
    implementation(Constants.TIMBERKT)

    testImplementation(Constants.JUNIT)
    testImplementation(Constants.ANDROIDX_TEST)
    testImplementation(Constants.ANDROIDX_TEST_JUNIT)
    testImplementation(Constants.ROBOLECTRIC)
}