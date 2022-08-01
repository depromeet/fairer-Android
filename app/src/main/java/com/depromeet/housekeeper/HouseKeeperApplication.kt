package com.depromeet.housekeeper

import android.app.Application
import com.depromeet.housekeeper.util.PrefsManager
import com.kakao.sdk.common.KakaoSdk
import timber.log.Timber

class HouseKeeperApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    PrefsManager.init(applicationContext)
    Timber.plant(Timber.DebugTree())
    KakaoSdk.init(this, resources.getString(R.string.KAKAO_NATIVE_APP_KEY))
  }
}