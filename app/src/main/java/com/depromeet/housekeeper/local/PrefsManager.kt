package com.depromeet.housekeeper.local

import android.content.Context
import android.content.SharedPreferences

object PrefsManager {
  private lateinit var prefs: SharedPreferences

  private const val ACCESS_TOKEN = "ACCESS_TOKEN"
  private const val REFRESH_TOKEN = "REFRESH_TOKEN"
  private const val USER_NAME = "USER_NAME"

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
}
