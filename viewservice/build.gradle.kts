plugins {
    id ("com.android.library")
    kotlin ("android")
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
}

dependencies {
    implementation(project(":core"))

    implementation(Constants.KOTLIN_JDK_8)
    implementation(Constants.ANDROID_LIFECYCLE_SERVICE)

    implementation(Constants.KOIN_ANDROID)
    implementation(Constants.KOIN_ANDROID_EXT)
    testImplementation(Constants.KOIN_TEST)
    testImplementation(Constants.KOIN_TEST_JUNIT4)

    implementation(platform(Constants.COROUTINES_BOM))

    implementation(Constants.COROUTINES)
    implementation(Constants.COROUTINES_ANDROID)
    testImplementation(Constants.COROUTINES_TEST)

    testImplementation(Constants.JUNIT)
    testImplementation(Constants.ROBOLECTRIC)
    testImplementation(Constants.ANDROIDX_TEST)
    testImplementation(Constants.ANDROIDX_TEST_RUNNER)
    testImplementation(Constants.ANDROIDX_TEST_RULES)
    testImplementation(Constants.ANDROIDX_TEST_JUNIT)
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
                artifactId = "view-service"
                version = Constants.VERSION_NAME

                pom {
                    name.set("View Service")
                    description.set("Create and attach Views easily to the WindowManager")

                    licenses {
                        license {
                            name.set("Apache-2.0")
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