plugins {
    id("com.android.library")
    kotlin("android")
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

tasks.dokkaHtml.configure {
    outputDirectory.set(file("$rootDir/docs/oauth2client"))

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
                artifactId = "oauth2client"
                version = Constants.VERSION_NAME

                pom {
                    name.set("OAuth2 Client")
                    description.set("OAuth 2 Clients for various Platforms")

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