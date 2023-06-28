package com.depromeet.housekeeper.util

import android.content.Context
import android.content.SharedPreferences
import com.depromeet.housekeeper.model.response.ProfileData

object PrefsManager {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("house_keeper", Context.MODE_PRIVATE)
    }

    val accessToken: String
        get() = prefs.getString(PREFS_ACCESS_TOKEN, "").toString()

    val refreshToken: String
        get() = prefs.getString(PREFS_REFRESH_TOKEN, "").toString()

    val deviceToken: String
        get() = prefs.getString(PREFS_DEVICE_TOKEN, "").toString()

    fun setAccessToken(accessToken: String, expiredTime: String){
        prefs.edit().apply {
            putString(PREFS_ACCESS_TOKEN, accessToken)
            putString(PREFS_ACCESS_TOKEN_EXPIRED_TIME, expiredTime)
        }.apply()
    }

    fun setRefreshToken(refreshToken: String, expiredTime: String){
        prefs.edit().apply {
            putString(PREFS_REFRESH_TOKEN, refreshToken)
            putString(PREFS_REFRESH_TOKEN_EXPIRED_TIME, expiredTime)
        }.apply()
    }

    fun setDeviceToken(deviceToken: String) {
        prefs.edit()?.apply {
            putString(PREFS_DEVICE_TOKEN, deviceToken)
        }?.apply()
    }

    fun deleteTokens() {
        prefs.edit()?.apply {
            remove(PREFS_ACCESS_TOKEN)
            remove(PREFS_ACCESS_TOKEN_EXPIRED_TIME)
            remove(PREFS_REFRESH_TOKEN)
            remove(PREFS_REFRESH_TOKEN_EXPIRED_TIME)
        }?.apply()
    }

    fun deleteMemberInfo() {
        prefs.edit()?.apply {
            remove(PREFS_USER_NAME)
            remove(PREFS_MEMBER_ID)
        }?.apply()
    }

    val userName: String
        get() = prefs.getString(PREFS_USER_NAME, PREFS_USER_NAME_DEFAULT).toString()

    fun setUserName(userName: String) {
        prefs.edit()?.apply {
            putString(PREFS_USER_NAME, userName)
        }?.apply()
    }

    val hasTeam: Boolean
        get() = prefs.getBoolean(PREFS_HAS_TEAM, false)

    fun setHasTeam(hasTeam: Boolean) {
        prefs.edit()?.apply {
            putBoolean(PREFS_HAS_TEAM, hasTeam)
        }?.apply()
    }

    val authCode: String
        get() = prefs.getString(PREFS_AUTH_CODE, "").toString()

    fun setAuthCode(authCode: String) {
        prefs.edit()?.apply {
            putString(PREFS_AUTH_CODE, authCode)
        }?.apply()
    }

    val memberId: Int
        get() = prefs.getInt(PREFS_MEMBER_ID, -1)

    fun setMemberId(memberId: Int) {
        prefs.edit()?.apply {
            putInt(PREFS_MEMBER_ID, memberId)
        }?.apply()
    }

    fun setUserProfile(profile: ProfileData) {
        prefs.edit().putString(PREFS_USER_NAME, profile.memberName).apply()
        prefs.edit().putString(PREFS_USER_PROFILE_PATH, profile.profilePath).apply()
        prefs.edit().putString(PREFS_STATUS_MESSAGE, profile.statusMessage).apply()
    }

    fun getUserProfile(): ProfileData {
        val memberName = prefs.getString(PREFS_USER_NAME, "")!!
        val profilePath = prefs.getString(PREFS_USER_PROFILE_PATH, "")!!
        val statusMessage = prefs.getString(PREFS_STATUS_MESSAGE, "")!!
        return ProfileData(memberName, profilePath, statusMessage)
    }


    fun setUserProfilePath(path: String) {
        prefs.edit().putString(PREFS_USER_PROFILE_PATH, path).apply()
    }

}
