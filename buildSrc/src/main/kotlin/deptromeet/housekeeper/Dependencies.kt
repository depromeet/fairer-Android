package deptromeet.housekeeper

object Dependencies {
    private const val CORE_KTX = "1.7.0"
    const val coreKtx = "androidx.core:core-ktx:$CORE_KTX"

    private const val APPCOMPAT = "1.4.1"
    const val appcompat = "androidx.appcompat:appcompat:$APPCOMPAT"

    private const val MATERIAL = "1.4.0"
    const val material = "com.google.android.material:material:$MATERIAL"

    private const val CONSTRAINT_LAYOUT = "2.1.3"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:$CONSTRAINT_LAYOUT"

    private const val JUNIT = "4.13.2"
    const val junit = "junit:junit:$JUNIT"

    private const val TEST_EXT_JUNIT = "1.1.3"
    const val testExtJunit = "androidx.test.ext:junit:$TEST_EXT_JUNIT"

    private const val ESPRESSO_CORE = "3.4.0"
    const val espressoCore = "androidx.test.espresso:espresso-core:$ESPRESSO_CORE"

    private const val TIMBER = "4.7.1"
    const val timber = "com.jakewharton.timber:timber:$TIMBER"

    private const val LIFECYCLE_EXTENSIONS = "2.2.0"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$LIFECYCLE_EXTENSIONS"

    private const val ACTIVITY_KTX = "1.4.0"
    const val activityKtx = "androidx.activity:activity-ktx:$ACTIVITY_KTX"

    private const val FRAGMENT_KTX = "1.4.1"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:$FRAGMENT_KTX"

    private const val ROOM = "2.4.1"
    const val roomRuntime = "androidx.room:room-runtime:$ROOM"
    const val roomCompiler = "androidx.room:room-compiler:$ROOM"
    const val roomKtx = "androidx.room:room-ktx:$ROOM"
    const val roomGuava = "androidx.room:room-guava:$ROOM"

    private const val COROUTINES = "1.5.0"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$COROUTINES"

    private const val NAV_VERSION = "2.5.2"
    const val navVersionKtx = "androidx.navigation:navigation-fragment-ktx:$NAV_VERSION"
    const val navVersionUi = "androidx.navigation:navigation-ui-ktx:$NAV_VERSION"
    const val navVersionDynamic =
        "androidx.navigation:navigation-dynamic-features-fragment:$NAV_VERSION"
    const val navVersionSafeArgs =
        "androidx.navigation:navigation-safe-args-gradle-plugin:$NAV_VERSION"


    private const val OKHTTP = "4.3.1"
    const val okhttp = "com.squareup.okhttp3:okhttp:OKHTTP"
    const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:$OKHTTP"

    private const val RETROFIT = "2.9.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:$RETROFIT"
    const val retrofitAdapter = "com.squareup.retrofit2:adapter-rxjava2:$RETROFIT"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:$RETROFIT"

    private const val COROUTINE_ADAPTER = "0.9.2"
    const val coroutineAdapter =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:$COROUTINE_ADAPTER"

    private const val MOSHI = "1.14.0"
    const val moshi = "com.squareup.moshi:moshi:$MOSHI"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:$MOSHI"
    const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:$MOSHI"

    private const val SERIALIZATION = "1.3.2"
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:$SERIALIZATION"

    private const val GOOGLELOGINSERVICE = "20.2.0"
    const val googlelogin = "com.google.android.gms:play-services-auth:$GOOGLELOGINSERVICE"

    private const val DATASTORE = "1.0.0"
    const val preferencesDatastore = "androidx.datastore:datastore-preferences:$DATASTORE"

    private const val SPLASHSCREEN = "1.0.0-beta02"
    const val splashscreen = "androidx.core:core-splashscreen:$SPLASHSCREEN"

    private const val KAKAO_SDK = "2.10.0"
    const val kakaoSdk = "com.kakao.sdk:v2-link:$KAKAO_SDK"

    private const val FIREBASE_SDK = "4.3.10"
    const val firebaseSdk = "com.google.gms:google-services:$FIREBASE_SDK"

    private const val FIREBASE_BOM = "30.0.2"
    private const val FIREBASE_MESSAGING = "23.0.6"
    private const val FIREBASE_MESSAGING_DIRECTBOOT = "20.2.0"
    const val firebaseBOM = "com.google.firebase:firebase-bom:$FIREBASE_BOM"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebasedynamiclink = "com.google.firebase:firebase-dynamic-links-ktx"
    const val firebasemessaging = "com.google.firebase:firebase-messaging-ktx:$FIREBASE_MESSAGING"
    const val firebaseMessagingDirectboot =
        "com.google.firebase:firebase-messaging-directboot:$FIREBASE_MESSAGING_DIRECTBOOT"
    const val firebaseConfig = "com.google.firebase:firebase-config-ktx"

    private const val GLIDE = "4.13.0"
    const val glide = "com.github.bumptech.glide:glide:$GLIDE"
    const val glidecompiler = "com.github.bumptech.glide:compiler:$GLIDE"
    const val glideannotation = "com.github.bumptech.glide:annotations:$GLIDE"
    const val glidesvgloader = "com.caverock:androidsvg:1.2.1"

    private const val WORKER_MANAGER = "2.7.1"
    const val workermanager = "androidx.work:work-runtime-ktx:$WORKER_MANAGER"

    private const val COIL = "2.1.0"
    const val coil = "io.coil-kt:coil:$COIL"

    private const val HILT = "2.43.2"
    const val hilt = "com.google.dagger:hilt-android:$HILT"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:$HILT"
    const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$HILT"

    private const val FLEXBOX = "3.0.0"
    const val flexbox = "com.google.android.flexbox:flexbox:$FLEXBOX"

    private const val ROULETTE = "0.3.0"
    const val roulette =  "com.github.mmoamenn:LuckyWheel_Android:$ROULETTE"
}