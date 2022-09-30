package com.depromeet.housekeeper

import android.app.Application
import android.content.Intent
import com.depromeet.housekeeper.service.InternetService
import com.depromeet.housekeeper.util.PrefsManager
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class HouseKeeperApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PrefsManager.init(applicationContext)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        KakaoSdk.init(this, resources.getString(R.string.KAKAO_NATIVE_APP_KEY))

        applicationContext.startService(Intent(this, InternetService::class.java))
    }
}