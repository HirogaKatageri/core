object Constants {

    const val MIN_SDK_VERSION = 23
    const val COMPILE_SDK_VERSION = 30
    const val TARGET_SDK_VERSION = 30
    const val VERSION_CODE = 5

    const val CORE_VERSION_NAME = "0.2.2"
    const val VIEW_SERVICE_VERSION_NAME = "0.1.0"
    const val OAUTH2_CLIENT_VERSION_NAME = "0.2.1"
    const val SAMPLE_VERSION_NAME = "0.2.1"

    //Google
    const val GOOGLE_SERVICES_VERSION = "4.3.4"

    //Kotlin
    const val KOTLIN_VERSION = "1.5.10"
    const val COROUTINES_VERSION = "1.4.3"

    //Android
    const val LIFECYCLE_VERSION = "2.3.0"
    const val ARCHITECTURE_VERSION = "2.1.0"
    const val NAVIGATION_VERSION = "2.3.5"
    const val CAMERAX_VERSION  = "1.0.0"

    //Android UI
    const val MATERIAL_VERSION = "1.2.1"
    const val RECYCLERVIEW_VERSION = "1.1.0"
    const val CARDVIEW_VERSION = "1.0.0"
    const val CONSTRAINTLAYOUT_VERSION = "2.0.4"

    //Core
    const val KOIN_VERSION = "3.0.2"
    const val ROOM_VERSION = "2.2.5"
    const val EPOXY_VERSION = "4.1.0"
    const val PAGING_VERSION = "2.1.2"
    const val RTMP_VERSION = "1.9.9"

    //Network
    const val RETROFIT_VERSION = "2.9.0"
    const val OKHTTP_VERSION = "4.9.0"
    const val MOSHI_VERSION = "1.11.0"

    //Logging
    const val TIMBERKT_VERSION = "1.5.1"
    const val CHUCK_VERSION = "1.1.0"

    //Testing
    const val ANDROIDX_TEST_VERSION = "1.3.0"
    const val ESPRESSO_VERSION = "3.3.0"
    const val ROBOLECTRIC_VERSION = "4.5.1"

    //Full Paths
    //Kotlin
    const val KOTLIN_JDK = "org.jetbrains.kotlin:kotlin-stdlib:$KOTLIN_VERSION"
    const val KOTLIN_JDK_8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$KOTLIN_VERSION"

    const val COROUTINES_BOM = "org.jetbrains.kotlinx:kotlinx-coroutines-bom:$COROUTINES_VERSION"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core"
    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android"
    const val COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test"

    //Android Essentials
    const val APPCOMPAT = "androidx.appcompat:appcompat:1.2.0"
    const val ANDROID_SECURITY = "androidx.security:security-crypto:1.0.0"

    //Android UI
    const val MATERIAL = "com.google.android.material:material:${MATERIAL_VERSION}"
    const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:${RECYCLERVIEW_VERSION}"
    const val CARDVIEW = "androidx.cardview:cardview:${CARDVIEW_VERSION}"
    const val CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${CONSTRAINTLAYOUT_VERSION}"
    const val VIEWPAGER_2 = "androidx.viewpager2:viewpager2:1.0.0"

    //Android Navigation
    const val NAVIGATION_FRAGMENT =
        "androidx.navigation:navigation-fragment-ktx:$NAVIGATION_VERSION"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:$NAVIGATION_VERSION"
    const val NAVIGATION_DYNAMIC =
        "androidx.navigation:navigation-dynamic-features-fragment:$NAVIGATION_VERSION"
    const val NAVIGATION_TESTING = "androidx.navigation:navigation-testing:$NAVIGATION_VERSION"

    //Android KTX
    const val ANDROID_KTX_CORE = "androidx.core:core-ktx:1.3.2"
    const val ANDROID_KTX_COLLECTIONS = "androidx.collection:collection-ktx:1.1.0"
    const val ANDROID_KTX_ACTIVITY = "androidx.activity:activity-ktx:1.2.2"
    const val ANDROID_KTX_FRAGMENT = "androidx.fragment:fragment-ktx:1.3.0"

    //Android Lifecycle Runtime
    const val ANDROID_LIFECYCLE = "androidx.lifecycle:lifecycle-runtime-ktx:$LIFECYCLE_VERSION"

    //Android Lifecycle Compiler
    const val ANDROID_LIFECYCLE_COMPILER =
        "androidx.lifecycle:lifecycle-compiler:$LIFECYCLE_VERSION" // JDK 7 and below kapt
    const val ANDROID_LIFECYCLE_COMMON =
        "androidx.lifecycle:lifecycle-common-java8:$LIFECYCLE_VERSION" // JDK 8 and above implementation

    //Android Lifecycle Modules
    const val ANDROID_LIFECYCLE_SERVICE = "androidx.lifecycle:lifecycle-service:$LIFECYCLE_VERSION"
    const val ANDROID_LIFECYCLE_APP = "androidx.lifecycle:lifecycle-process:$LIFECYCLE_VERSION"
    const val ANDROID_LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:$LIFECYCLE_VERSION"
    const val ANDROID_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VERSION"
    const val ANDROID_STATE_VIEWMODEL =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:$LIFECYCLE_VERSION"
    const val ANDROID_COMPOSE_VIEWMODEL =
        "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha01"

    //Android Lifecycle Test Helpers
    const val ANDROID_LIFECYCLE_TEST = "androidx.arch.core:core-testing:$ARCHITECTURE_VERSION"

    //Android CameraX
    const val ANDROID_CAMERAX_CORE = "androidx.camera:camera-core:$CAMERAX_VERSION"
    const val ANDROID_CAMERAX_CAMERA2 = "androidx.camera:camera-camera2:$CAMERAX_VERSION"
    const val ANDROID_CAMERAX_LIFECYCLE = "androidx.camera:camera-lifecycle:$CAMERAX_VERSION"
    const val ANDROID_CAMERAX_VIEW = "androidx.camera:camera-view:1.0.0-alpha24"

    //DI
    const val KOIN_CORE = "io.insert-koin:koin-core:$KOIN_VERSION"
    const val KOIN_TEST = "io.insert-koin:koin-test:$KOIN_VERSION"
    const val KOIN_TEST_JUNIT4 = "io.insert-koin:koin-test-junit4:$KOIN_VERSION" // Tests
    const val KOIN_ANDROID = "io.insert-koin:koin-android:$KOIN_VERSION"
    const val KOIN_ANDROID_EXT = "io.insert-koin:koin-android-ext:$KOIN_VERSION"
    const val KOIN_ANDROID_WORKMANAGER = "io.insert-koin:koin-androidx-workmanager:$KOIN_VERSION"
    const val KOIN_ANDROID_COMPOSE = "io.insert-koin:koin-androidx-compose:$KOIN_VERSION"

    //Data Management
    const val ROOM_CORE = "androidx.room:room-runtime:$ROOM_VERSION"
    const val ROOM_COMPILER = "androidx.room:room-compiler:$ROOM_VERSION"
    const val ROOM_KTX = "androidx.room:room-ktx:$ROOM_VERSION"
    const val ROOM_TEST = "androidx.room:room-testing:$ROOM_VERSION"

    const val PAGING = "androidx.paging:paging-runtime-ktx:$PAGING_VERSION"
    const val PAGING_TEST = "androidx.paging:paging-common-ktx:$PAGING_VERSION"

    //RecyclerView Util
    const val EPOXY = "com.airbnb.android:epoxy:$EPOXY_VERSION"
    const val EPOXY_PAGING = "com.airbnb.android:epoxy-paging:$EPOXY_VERSION"
    const val EPOXY_PROCESSOR = "com.airbnb.android:epoxy-processor:$EPOXY_VERSION"

    //Image Utils
    const val COIL = "io.coil-kt:coil:1.0.0"

    //Misc. Utils
    const val DATE_TIME = "com.jakewharton.threetenabp:threetenabp:1.3.0"
    const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:2.7"

    //Firebase
    const val FIREBASE_AUTH = "com.google.firebase:firebase-auth:19.3.2"

    //Authentication
    const val APP_AUTH = "net.openid:appauth:0.9.0"

    //Network
    const val MOSHI = "com.squareup.moshi:moshi-kotlin:$MOSHI_VERSION"
    const val MOSHI_CODEGEN = "com.squareup.moshi:moshi-kotlin-codegen:$MOSHI_VERSION"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:$RETROFIT_VERSION"
    const val RETROFIT_MOSHI = "com.squareup.retrofit2:converter-moshi:$RETROFIT_VERSION"
    const val RETROFIT_MOCK = "com.squareup.retrofit2:retrofit-mock:$RETROFIT_VERSION"
    const val OKHTTP = "com.squareup.okhttp3:okhttp:$OKHTTP_VERSION"
    const val OKHTTP_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:$OKHTTP_VERSION"
    const val OKHTTP_URL_CONNECTION = "com.squareup.okhttp3:okhttp-urlconnection:$OKHTTP_VERSION"
    const val OKHTTP_MOCK_SERVER = "com.squareup.okhttp3:mockwebserver:$OKHTTP_VERSION"
    const val COOKIE_STORE = "net.gotev:cookie-store:1.2.1"
    const val NETWORK_RESPONSE_ADAPTER = "com.github.haroldadmin:NetworkResponseAdapter:4.1.0"

    //Logging
    const val TIMBERKT = "com.github.ajalt:timberkt:$TIMBERKT_VERSION"
    const val CHUCK_DEBUG = "com.readystatesoftware.chuck:library:$CHUCK_VERSION"
    const val CHUCK_RELEASE = "com.readystatesoftware.chuck:library-no-op:$CHUCK_VERSION"

    //Test
    const val JUNIT = "junit:junit:4.13.2" // Unit Test available
    const val ANDROIDX_TEST = "androidx.test:core:$ANDROIDX_TEST_VERSION"
    const val ANDROIDX_TEST_FRAGMENT = "androidx.fragment:fragment-testing:$ANDROIDX_TEST_VERSION"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:$ANDROIDX_TEST_VERSION"
    const val ANDROIDX_TEST_RULES = "androidx.test:rules:$ANDROIDX_TEST_VERSION"
    const val ANDROIDX_TEST_JUNIT = "androidx.test.ext:junit:1.1.2"

    const val ESPRESSO = "androidx.test.espresso:espresso-core:$ESPRESSO_VERSION"
    const val ESPRESSO_INTENTS = "androidx.test.espresso:espresso-intents:3.1.0"

    const val ROBOLECTRIC =
        "org.robolectric:robolectric:$ROBOLECTRIC_VERSION" // Unit Test available
}