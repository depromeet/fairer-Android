package com.depromeet.housekeeper

import android.app.Application
import timber.log.Timber

class HouseKeeperApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
  }
}