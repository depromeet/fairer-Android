package com.depromeet.housekeeper.local

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


  fun init(context: Context) {
    prefs = context.getSharedPreferences("house_keeper", Context.MODE_PRIVATE)
  }

  val accessToken: String
    get() = prefs.getString(ACCESS_TOKEN, "").toString()

  val refreshToken: String
    get() = prefs.getString(REFRESH_TOKEN, "").toString()

  fun setTokens(accessToken: String, refreshToken: String) {
    prefs.edit()?.apply {
      putString(ACCESS_TOKEN, accessToken)
      putString(REFRESH_TOKEN, refreshToken)
    }?.apply()
  }

  fun deleteTokens() {
    prefs.edit()?.apply {
      remove(ACCESS_TOKEN)
      remove(REFRESH_TOKEN)
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

  // TODO : memberId 저장 시점
  val memberId: Int
    get() = prefs.getInt(MEMBER_ID, 10)

  fun setMemberId(memberId: Int) {
    prefs.edit()?.apply {
      putInt(MEMBER_ID, memberId)
    }?.apply()
  }

  fun deleteMemberInfo() {
    prefs.edit()?.apply {
      remove(MEMBER_ID)
      setHasTeam(false)
    }?.apply()
  }

}
