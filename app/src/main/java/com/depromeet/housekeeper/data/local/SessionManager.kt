package com.depromeet.housekeeper.data.local

import android.content.Context
import android.content.Intent
import com.depromeet.housekeeper.ui.signIn.LoginFragment
import com.depromeet.housekeeper.util.PrefsManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.sync.Mutex
import timber.log.Timber
import javax.inject.Inject

class SessionManager @Inject constructor(val context: Context){
    companion object {
        var logoutFlag = false
    }

    fun getAccessToken(): String? = PrefsManager.accessToken ?: PrefsManager.authCode

    fun updateAccessToken(accessToken: String) = PrefsManager.setAccessToken(accessToken)

    fun getRefreshToken(): String? = PrefsManager.refreshToken

    fun refreshToken(refreshToken: String): String {
        PrefsManager.deleteTokens()
        return refreshToken
    }

    fun logout() {
        if (logoutFlag) {
            Timber.e("작업중~~~~ HTTP_UNAUTHORIZED")
            return
        }

        logoutFlag = true

        PrefsManager.clearPrefs()
        LoginFragment.mGoogleSignInClient.signOut().addOnCanceledListener {
            // 앱 재시작
            val result = kotlin.runCatching {
                val componentName =
                    context.packageManager.getLaunchIntentForPackage(context.packageName)?.component
                val mainIntent = Intent.makeRestartActivityTask(componentName)
                context.startActivity(mainIntent)
            }
            if (result.isSuccess) logoutFlag = false
        }



    }

}