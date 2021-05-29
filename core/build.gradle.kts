plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("org.jetbrains.dokka")
    `maven-publish`
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
    buildFeatures {
        viewBinding = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    lintOptions {
        disable(
            "UnsafeExperimentalUsageError",
            "UnsafeExperimentalUsageWarning"
        )
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Constants.KOTLIN_JDK_8)
    implementation(Constants.APPCOMPAT)
    implementation(Constants.MATERIAL)

    implementation(Constants.ANDROID_KTX_ACTIVITY)
    implementation(Constants.ANDROID_KTX_FRAGMENT)

    implementation(Constants.ANDROID_LIVEDATA)
    implementation(Constants.ANDROID_VIEWMODEL)
    implementation(Constants.ANDROID_STATE_VIEWMODEL)
    implementation(Constants.ANDROID_LIFECYCLE_SERVICE)

    implementation(platform(Constants.COROUTINES_BOM))

    implementation(Constants.COROUTINES)
    implementation(Constants.COROUTINES_ANDROID)
    testImplementation(Constants.COROUTINES_TEST)

    implementation(Constants.TIMBERKT)

    api(Constants.KOIN_ANDROID)
    api(Constants.KOIN_ANDROID_SCOPE)
    api(Constants.KOIN_ANDROID_VIEWMODEL)
    api(Constants.KOIN_ANDROID_FRAGMENT)
    api(Constants.KOIN_ANDROID_EXT)
    testImplementation(Constants.KOIN_TEST)

    testImplementation(Constants.JUNIT)
    testImplementation(Constants.ROBOLECTRIC)
    testImplementation(Constants.ANDROIDX_TEST)
    debugImplementation(Constants.ANDROIDX_TEST_FRAGMENT)
    testImplementation(Constants.ANDROIDX_TEST_RUNNER)
    testImplementation(Constants.ANDROIDX_TEST_RULES)
    testImplementation(Constants.ANDROIDX_TEST_JUNIT)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(file("$rootDir/docs"))

    dokkaSourceSets {
        named("main") {
            includeNonPublic.set(true)
            jdkVersion.set(8)
            noStdlibLink.set(false)
            noJdkLink.set(false)
            noAndroidSdkLink.set(false)
        }
    }
    pluginsMapConfiguration.set(
        mapOf(
            "org.jetbrains.dokka.base.DokkaBase" to """{ "separateInheritedMembers":true}"""
        )
    )
}

afterEvaluate {
    publishing {
        publications {

            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {

                // Applies the component for the release build variant.
                from(components["release"])

                // You can then customize attributes of the publication as shown below.
                groupId = "dev.hirogakatageri"
                artifactId = "core"
                version = Constants.VERSION_NAME

                pom {
                    name.set("Core")
                    description.set("Boilerplate Library for Android using MVVM and Koin DI.")

                    licenses {
                        license {
                            name.set("The MIT License")
                            url.set("https://github.com/HirogaKatageri/core/blob/master/LICENSE.md")
                        }
                    }

                    developers {
                        developer {
                            id.set("HirogaKatageri")
                            name.set("Gian Patrick Quintana")
                            email.set("gianpatrick27@gmail.com")
                        }
                    }
                }
            }
        }

        repositories {
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
}