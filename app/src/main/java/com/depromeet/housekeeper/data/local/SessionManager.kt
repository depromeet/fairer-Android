package com.depromeet.housekeeper.data.local

import com.depromeet.housekeeper.util.PrefsManager

class SessionManager {
    fun getAccessToken(): String? = PrefsManager.accessToken

    fun getRefreshToken(): String? = PrefsManager.refreshToken

    fun logout() = PrefsManager.deleteTokens()

}