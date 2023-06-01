package com.depromeet.housekeeper.di

import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.util.FIREBASE_SERVER_URL
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigWrapper @Inject constructor() {
    private val remoteConfig: FirebaseRemoteConfig by lazy{
        FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .build()
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_default) // 먼저 안전하게 default 가져옴(remote config 변경되면 xml 변경해주기를 요망)
        }
    }

    fun fetchAndActivateConfig(): String {
        remoteConfig.fetchAndActivate().addOnCompleteListener{ task ->
            val updated = task.result
            if (task.isSuccessful){
                val updated = task.result
                Timber.d("Config params updated success: $updated")
            } else {
                Timber.d("Config params updated failed: $updated")
            }
        }
        return remoteConfig.getString(FIREBASE_SERVER_URL)
    }
}
