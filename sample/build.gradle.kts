plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("org.jlleitschuh.gradle.ktlint")
}

val buildProperties = BuildHelper.getBuildProperties()
val keyStoreProperties = BuildHelper.getKeyStoreProperties()

android {
    compileSdk = Constants.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "dev.hirogakatageri.sandbox"
        minSdk = Constants.MIN_SDK_VERSION
        targetSdk = Constants.TARGET_SDK_VERSION

        versionCode = Constants.VERSION_CODE
        versionName = Constants.SAMPLE_VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "TWITCH_CLIENT_ID",
            buildProperties.getProperty("TWITCH_CLIENT_ID", "")
        )

        buildConfigField(
            "String",
            "TWITCH_SECRET_KEY",
            buildProperties.getProperty("TWITCH_SECRET_KEY", "")
        )

        resConfigs("en")
    }

    signingConfigs {
        create("release") {
            storeFile = file("../signature/core-keystore.jks")
            storePassword = keyStoreProperties.getProperty("CORE_KEY_PASSWORD")
            keyAlias = keyStoreProperties.getProperty("CORE_ALIAS")
            keyPassword = keyStoreProperties.getProperty("CORE_KEY_PASSWORD")
        }
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
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                when (requested.name) {
                    "kotlin-reflect" -> useVersion(Constants.KOTLIN_VERSION)
                }
            }
        }
    }

    applicationVariants.all {
        outputs.forEach { output ->
            if (output is com.android.build.gradle.internal.api.BaseVariantOutputImpl) {
                val packageName = "dev.hirogakatageri.sandbox"
                val buildName = output.name
                val versionName = Constants.SAMPLE_VERSION_NAME

                val format = "%s-%s-v%s.apk"
                val outputFileName = String.format(format, packageName, buildName, versionName)

                output.outputFileName = outputFileName
            }
        }
    }
}

dependencies {

    implementation(project(":oauth2client"))
    implementation("com.github.HirogaKatageri.core:core:0.3.5")
    implementation("com.github.HirogaKatageri:view-service:0.1.2")

    // Kotlin
    implementation(Constants.KOTLIN_JDK_8)

    implementation(platform(Constants.COROUTINES_BOM))

    implementation(Constants.COROUTINES)
    implementation(Constants.COROUTINES_ANDROID)
    testImplementation(Constants.COROUTINES_TEST)

    implementation(Constants.COROUTINES_PLAY_SERVICES)

    // Koin
    implementation(Constants.KOIN_ANDROID)
    testImplementation(Constants.KOIN_TEST)
    testImplementation(Constants.KOIN_TEST_JUNIT4)

    // Android
    implementation(Constants.APPCOMPAT)
    implementation(Constants.MATERIAL)
    implementation(Constants.CONSTRAINT_LAYOUT)

    // Android Kotlin Extensions
    implementation(Constants.ANDROID_KTX_CORE)
    implementation(Constants.ANDROID_KTX_ACTIVITY)
    implementation(Constants.ANDROID_KTX_FRAGMENT)

    // Google
    implementation(Constants.GOOGLE_PLAY_SERVICES)

    // Firebase
    implementation(platform(Constants.FIREBASE_BOM))
    implementation(Constants.FIREBASE_ANALYTICS)
    implementation(Constants.FIREBASE_CRASHLYTICS)
    implementation(Constants.FIREBASE_CLOUD_MESSAGING)
    implementation(Constants.FIREBASE_AUTH)
    implementation(Constants.FIREBASE_AUTH_UI)
    implementation(Constants.FIREBASE_FIRESTORE)

    // Android Lifecycle Libraries
    implementation(Constants.ANDROID_LIFECYCLE_SERVICE)
    implementation(Constants.ANDROID_STATE_VIEWMODEL)

    // Android Jetpack Navigation
    implementation(Constants.NAVIGATION_FRAGMENT)
    implementation(Constants.NAVIGATION_UI)
    androidTestImplementation(Constants.NAVIGATION_TESTING)

    // Android Camera
    implementation(Constants.ANDROID_CAMERAX_CORE)
    implementation(Constants.ANDROID_CAMERAX_CAMERA2)
    implementation(Constants.ANDROID_CAMERAX_LIFECYCLE)
    implementation(Constants.ANDROID_CAMERAX_VIEW)

    // RecyclerView
    implementation(Constants.EPOXY)
    kapt(Constants.EPOXY_PROCESSOR)

    // Core
    implementation(Constants.DATE_TIME)
    implementation(Constants.TIMBERKT)
    debugImplementation(Constants.LEAK_CANARY)

    // JSON Parser
    implementation(Constants.MOSHI)
    kapt(Constants.MOSHI_CODEGEN)

    // Http
    implementation(Constants.RETROFIT)
    implementation(Constants.RETROFIT_MOSHI)
    implementation(Constants.OKHTTP)
    implementation(Constants.OKHTTP_LOG_INTERCEPTOR)
    implementation(Constants.NETWORK_RESPONSE_ADAPTER)

    // Testing
    testImplementation(Constants.MOCKITO_CORE)
    testImplementation(Constants.MOCKITO_INLINE)
    testImplementation(Constants.JUNIT)
    testImplementation(Constants.ROBOLECTRIC)
    testImplementation(Constants.ANDROIDX_TEST)
    debugImplementation(Constants.ANDROIDX_TEST_FRAGMENT)
    testImplementation(Constants.ANDROIDX_TEST_RUNNER)
    testImplementation(Constants.ANDROIDX_TEST_RULES)
    testImplementation(Constants.ANDROIDX_TEST_JUNIT)
}
