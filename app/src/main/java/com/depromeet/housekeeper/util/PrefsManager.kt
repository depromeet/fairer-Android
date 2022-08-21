package com.depromeet.housekeeper.util

import android.content.Context
import android.content.SharedPreferences

object PrefsManager {
  private lateinit var prefs: SharedPreferences

  private const val ACCESS_TOKEN = "ACCESS_TOKEN"
  private const val REFRESH_TOKEN = "REFRESH_TOKEN"
  private const val USER_NAME = "USER_NAME"
  private const val HAS_TEAM = "HAS_TEAM"
  private const val AUTH_CODE = "AUTH_CODE"
  private const val MEMBER_ID = "MEMBER_ID"
  private const val DEVICE_TOKEN = "DEViCE_TOKEN"

  fun init(context: Context) {
    prefs = context.getSharedPreferences("house_keeper", Context.MODE_PRIVATE)
  }

  val accessToken: String
    get() = prefs.getString(ACCESS_TOKEN, "").toString()

  val refreshToken: String
    get() = prefs.getString(REFRESH_TOKEN, "").toString()

  val deviceToken: String
    get() = prefs.getString(DEVICE_TOKEN, "").toString()

  fun setTokens(accessToken: String, refreshToken: String) {
    prefs.edit()?.apply {
      putString(ACCESS_TOKEN, accessToken)
      putString(REFRESH_TOKEN, refreshToken)
    }?.apply()
  }

  fun setDeviceToken(deviceToken: String) {
    prefs.edit()?.apply {
      putString(DEVICE_TOKEN, deviceToken)
    }?.apply()
  }

  fun deleteTokens() {
    prefs.edit()?.apply {
      remove(ACCESS_TOKEN)
      remove(REFRESH_TOKEN)
    }?.apply()
  }

  fun deleteMemberInfo() {
    prefs.edit()?.apply {
      remove(USER_NAME)
      remove(MEMBER_ID)
    }?.apply()
  }

  val userName: String
    get() = prefs.getString(USER_NAME, "User Name").toString()

  fun setUserName(userName: String) {
    prefs.edit()?.apply {
      putString(USER_NAME, userName)
    }?.apply()
  }

  val hasTeam: Boolean
    get() = prefs.getBoolean(HAS_TEAM, false)

  fun setHasTeam(hasTeam: Boolean) {
    prefs.edit()?.apply {
      putBoolean(HAS_TEAM, hasTeam)
    }?.apply()
  }

  val authCode: String
    get() = prefs.getString(AUTH_CODE, "").toString()

  fun setAuthCode(authCode: String) {
    prefs.edit()?.apply {
      putString(AUTH_CODE, authCode)
    }?.apply()
  }

  val memberId: Int
    get() = prefs.getInt(MEMBER_ID, -1)

  fun setMemberId(memberId: Int) {
    prefs.edit()?.apply {
      putInt(MEMBER_ID, memberId)
    }?.apply()
  }

}
