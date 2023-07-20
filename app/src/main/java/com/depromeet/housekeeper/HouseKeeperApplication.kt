package com.depromeet.housekeeper

import android.app.Application
import com.depromeet.housekeeper.util.PrefsManager
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class HouseKeeperApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PrefsManager.init(applicationContext)
        KakaoSdk.init(this, resources.getString(R.string.KAKAO_NATIVE_APP_KEY))
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}