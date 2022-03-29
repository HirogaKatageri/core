object Deps {

    object Kotlin {
        const val KOTLIN_BOM = "org.jetbrains.kotlin:kotlin-bom:${Versions.Kotlin.BOM}"
        const val KOTLIN_JDK8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
        const val COROUTINES_BOM =
            "org.jetbrains.kotlinx:kotlinx-coroutines-bom:${Versions.Kotlin.COROUTINES_BOM}"
        const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core"
        const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android"
        const val COROUTINES_PLAY_SERVICES =
            "org.jetbrains.kotlinx:kotlinx-coroutines-play-services"
    }

    object Google {
        const val GPLAY_SERVICES_AUTH =
            "com.google.android.gms:play-services-auth:${Versions.Google.PLAY_SERVICES}"
    }

    object Android {
        const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.Android.APPCOMPAT}"
        const val SECURITY = "androidx.security:security-crypto:${Versions.Android.SECURITY}"
        const val MATERIAL = "com.google.android.material:material:${Versions.Android.MATERIAL}"
        const val RECYCLERVIEW =
            "androidx.recyclerview:recyclerview:${Versions.Android.RECYCLERVIEW}"
        const val CARDVIEW = "androidx.cardview:cardview:${Versions.Android.CARDVIEW}"
        const val CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${Versions.Android.CONSTRAINT_LAYOUT}"
        const val VIEWPAGER2 = "androidx.viewpager2:viewpager2:${Versions.Android.VIEWPAGER2}"
        const val NAVIGATION_FRAGMENT =
            "androidx.navigation:navigation-fragment-ktx:${Versions.Android.NAVIGATION}"
        const val NAVIGATION_UI =
            "androidx.navigation:navigation-ui-ktx:${Versions.Android.NAVIGATION}"
    }

    object AndroidLifecycle {
        const val COMMON = "androidx.lifecycle:lifecycle-common-java8:${Versions.Android.LIFECYCLE}"
        const val APP = "androidx.lifecycle:lifecycle-process:${Versions.Android.LIFECYCLE}"
        const val SERVICE = "androidx.lifecycle:lifecycle-service:${Versions.Android.LIFECYCLE}"
        const val LIVEDATA =
            "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.Android.LIFECYCLE}"
        const val VIEWMODEL =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Android.LIFECYCLE}"
        const val STATE_VIEWMODEL =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.Android.LIFECYCLE}"
    }

    object AndroidCamera {
        const val CORE = "androidx.camera:camera-core:${Versions.Android.CAMERAX}"
        const val CAMERA2 = "androidx.camera:camera-camera2:${Versions.Android.CAMERAX}"
        const val LIFECYCLE = "androidx.camera:camera-lifecycle:${Versions.Android.CAMERAX}"
        const val VIEW = "androidx.camera:camera-view:1.0.0-alpha32"
    }

    object AndroidKtx {
        const val CORE = "androidx.core:core-ktx:${Versions.Android.CORE_KTX}"
        const val COLLECTION =
            "androidx.collection:collection-ktx:${Versions.Android.COLLECTION_KTX}"
        const val ACTIVITY = "androidx.activity:activity-ktx:${Versions.Android.ACTIVITY_KTX}"
        const val FRAGMENT = "androidx.fragment:fragment-ktx:${Versions.Android.FRAGMENT_KTX}"
    }

    object Firebase {
        const val BOM = "com.google.firebase:firebase-bom:${Versions.Firebase.BOM}"
        const val ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
        const val CRASHLYTICS = "com.google.firebase:firebase-crashlytics-ktx"
        const val CLOUD_MESSAGING = "com.google.firebase:firebase-messaging-ktx"
        const val FIRESTORE = "com.google.firebase:firebase-firestore-ktx"
        const val AUTH = "com.google.firebase:firebase-auth-ktx"
        const val AUTH_UI = "com.firebaseui:firebase-ui-auth:8.0.0"
    }

    object Koin {
        const val CORE = "io.insert-koin:koin-core:${Versions.DI.KOIN}"
        const val KTOR = "io.insert-koin:koin-ktor:${Versions.DI.KOIN}"
        const val SL4J = "io.insert-koin:koin-logger-slf4j:${Versions.DI.KOIN}"
        const val ANDROID = "io.insert-koin:koin-android:${Versions.DI.KOIN}"
        const val ANDROID_COMPAT = "io.insert-koin:koin-android-compat:${Versions.DI.KOIN}"
        const val WORKMANAGER = "io.insert-koin:koin-androidx-workmanager:${Versions.DI.KOIN}"
        const val NAVIGATION = "io.insert-koin:koin-androidx-navigation:${Versions.DI.KOIN}"
        const val COMPOSE = "io.insert-koin:koin-androidx-compose:${Versions.DI.KOIN}"
    }

    object Room {
        const val CORE = "androidx.room:room-runtime:${Versions.Android.ROOM}"
        const val KTX = "androidx.room:room-ktx:${Versions.Android.ROOM}"
        const val COMPILER = "androidx.room:room-compiler:${Versions.Android.ROOM}"
    }

    object Paging {
        const val CORE = "androidx.paging:paging-runtime-ktx:${Versions.Android.PAGING}"
    }

    object Epoxy {
        const val CORE = "com.airbnb.android:epoxy:${Versions.UI.EPOXY}"
        const val PAGING = "com.airbnb.android:epoxy-paging:${Versions.UI.EPOXY}"
        const val PROCESSOR = "com.airbnb.android:epoxy-processor:${Versions.UI.EPOXY}"
    }

    object Image {
        const val COIL = "io.coil-kt:coil:${Versions.UI.COIL}"
        const val PICKER = "com.github.Drjacky:ImagePicker:2.1.13"
    }

    object DebugTools {
        const val LEAK_CANARY =
            "com.squareup.leakcanary:leakcanary-android:${Versions.DebugTools.LEAK_CANARY}"
        const val CHUCKER = "com.github.chuckerteam.chucker:library:${Versions.DebugTools.CHUCKER}"
        const val CHUCKER_NOOP =
            "com.github.chuckerteam.chucker:library-noop:${Versions.DebugTools.CHUCKER}"
        const val TIMBER = "com.jakewharton.timber:timber:${Versions.DebugTools.TIMBER}"
    }

    object Security {
        const val APP_AUTH = "net.openid:appauth:${Versions.Security.APP_AUTH}"
    }

    object Network {
        const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.Network.RETROFIT}"
        const val RETROFIT_MOSHI = "com.squareup.retrofit2:converter-moshi:${Versions.Network.RETROFIT}"
        const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.Network.OKHTTP}"
        const val OKHTTP_LOGGING =
            "com.squareup.okhttp3:logging-interceptor:${Versions.Network.OKHTTP}"
        const val MOSHI = "com.squareup.moshi:moshi:${Versions.Network.MOSHI}"
        const val MOSHI_CODEGEN =
            "com.squareup.moshi:moshi-kotlin-codegen:${Versions.Network.MOSHI}"
        const val NETWORK_RESPONSE = "com.github.haroldadmin:NetworkResponseAdapter:4.2.2"
    }

    object Test {
        const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-test"
        const val NAVIGATION =
            "androidx.navigation:navigation-testing:${Versions.Android.NAVIGATION}"
        const val LIFECYCLE = "androidx.arch.core:core-testing:${Versions.Android.ARCHITECTURE}"
        const val KOIN_CORE = "org.koin:koin-test:${Versions.DI.KOIN}"
        const val KOIN_JUNIT4 = "org.koin:koin-test-junit4:${Versions.DI.KOIN}"
        const val KOIN_JUNIT5 = "io.insert-koin:koin-test-junit5:${Versions.DI.KOIN}"
        const val ROOM = "androidx.room:room-testing:${Versions.Android.ROOM}"
        const val PAGING = "androidx.paging:paging-common-ktx:${Versions.Android.PAGING}"
        const val MOCKK = "io.mockk:mockk:${Versions.Test.MOCKK}"
        const val ANDROIDX_CORE = "androidx.test:core:${Versions.Test.ANDROIDX}"
        const val ANDROIDX_RUNNER = "androidx.test:runner:${Versions.Test.ANDROIDX}"
        const val ANDROIDX_RULES = "androidx.test:rules:${Versions.Test.ANDROIDX}"
        const val ANDROIDX_FRAGMENT = "androidx.fragment:fragment-testing:1.4.0"
        const val ANDROIDX_JUNIT = "androidx.test.ext:junit:1.1.3"
        const val ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.Test.ESPRESSO}"
        const val ROBOLECTRIC = "org.robolectric:robolectric:${Versions.Test.ROBOLECTRIC}"
    }

}