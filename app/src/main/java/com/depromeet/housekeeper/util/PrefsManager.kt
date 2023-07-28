package com.depromeet.housekeeper.util

import android.content.Context
import android.content.SharedPreferences
import com.depromeet.housekeeper.model.response.ProfileData
import timber.log.Timber

object PrefsManager {
    private lateinit var prefs: SharedPreferences

    fun clearPrefs(){
        prefs.edit().clear().apply()
    }

    fun init(context: Context) {
        Timber.d("init prefs")
        prefs = context.getSharedPreferences("house_keeper", Context.MODE_PRIVATE)
    }

    val deviceToken: String?
        get() = prefs.getString(PREFS_DEVICE_TOKEN, null)

    val authCode: String?
        get() = prefs.getString(PREFS_AUTH_CODE, null)

    fun setAuthCode(authCode: String?) {
        prefs.edit()?.apply {
            putString(PREFS_AUTH_CODE, authCode)
        }?.apply()
    }


    fun setDeviceToken(deviceToken: String) {
        prefs.edit()?.apply {
            putString(PREFS_DEVICE_TOKEN, deviceToken)
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
