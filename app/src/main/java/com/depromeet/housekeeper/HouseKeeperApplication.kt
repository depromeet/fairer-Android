package com.depromeet.housekeeper

import android.app.Application
import com.depromeet.housekeeper.local.PrefsManager
import timber.log.Timber

class HouseKeeperApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    PrefsManager.init(applicationContext)
    Timber.plant(Timber.DebugTree())
  }
}