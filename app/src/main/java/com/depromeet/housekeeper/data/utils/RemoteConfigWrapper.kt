package com.depromeet.housekeeper.data.utils

import android.annotation.SuppressLint
import android.content.Context
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.util.FIREBASE_SERVER_URL
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigClientException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigWrapper @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val remoteConfig: FirebaseRemoteConfig by lazy{
        FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .build()
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_default) // 먼저 안전하게 default 가져옴(remote config 변경되면 xml 변경해주기를 요망)
        }
    }

    @SuppressLint("ResourceType")
    fun fetchAndActivateConfig(): String {
        try {
            remoteConfig.fetchAndActivate()
            return remoteConfig.getString(FIREBASE_SERVER_URL)
        } catch (e: FirebaseRemoteConfigClientException){
            Timber.e(e.message, "Firebase 가져오질 못했어요")
        } catch (e: Exception){
            Timber.e(e.message, e.stackTraceToString())
        }

        return context.getString(R.xml.remote_config_default)
    }
}
