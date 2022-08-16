package com.depromeet.housekeeper

import android.app.Application
import com.depromeet.housekeeper.local.PrefsManager
import com.kakao.sdk.common.KakaoSdk
import timber.log.Timber

class HouseKeeperApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    PrefsManager.init(applicationContext)
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
    KakaoSdk.init(this, resources.getString(R.string.KAKAO_NATIVE_APP_KEY))
  }
}