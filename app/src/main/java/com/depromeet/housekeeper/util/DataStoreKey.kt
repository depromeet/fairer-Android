package com.depromeet.housekeeper.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "loginToken")

object DataStoreKey {
    val AccessTokenKey = stringPreferencesKey("accesstoken")
    val RefreshTokenKey = stringPreferencesKey("refreshtoken")
}