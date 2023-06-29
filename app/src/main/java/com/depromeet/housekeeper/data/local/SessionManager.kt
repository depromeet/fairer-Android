package com.depromeet.housekeeper.data.local

import android.content.Context
import android.content.Intent
import com.depromeet.housekeeper.util.PrefsManager
import javax.inject.Inject

class SessionManager @Inject constructor(val context: Context){
    fun getAccessToken(): String = PrefsManager.accessToken ?: PrefsManager.authCode

    fun updateAccessToken(accessToken: String) = PrefsManager.setAccessToken(accessToken)

    fun getRefreshToken(): String? = PrefsManager.refreshToken

    fun refreshToken(refreshToken: String): String {
        PrefsManager.deleteTokens()
        return refreshToken
    }

    fun logout() {
        PrefsManager.deleteTokens()
        // 앱 재시작
        val componentName = context.packageManager.getLaunchIntentForPackage(context.packageName)?.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
    }

}