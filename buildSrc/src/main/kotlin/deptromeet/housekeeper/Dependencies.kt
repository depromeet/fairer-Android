package deptromeet.housekeeper

object Dependencies {
  private const val CORE_KTX = "1.7.0"
  const val coreKtx = "androidx.core:core-ktx:$CORE_KTX"

  private const val APPCOMPAT = "1.4.1"
  const val appcompat = "androidx.appcompat:appcompat:$APPCOMPAT"

  private const val MATERIAL = "1.5.0"
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

  private const val NAV_VERSION = "2.3.5"
  const val navVersionKtx = "androidx.navigation:navigation-fragment-ktx:$NAV_VERSION"
  const val navVersionUi = "androidx.navigation:navigation-ui-ktx:$NAV_VERSION"
  const val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:$NAV_VERSION"
  const val navVersionDynamicFeatures =
    "androidx.navigation:navigation-dynamic-features-fragment:$NAV_VERSION"
  const val navTesting = "androidx.navigation:navigation-testing:$NAV_VERSION"

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

  private const val MOSHI = "1.12.0"
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

  private const val GLIDE = "4.13.0"
  const val glide ="com.github.bumptech.glide:glide:$GLIDE"
  const val glideannotation ="com.github.bumptech.glide:compiler:$GLIDE"
}