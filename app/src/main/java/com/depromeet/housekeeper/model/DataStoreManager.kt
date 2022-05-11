package com.depromeet.housekeeper.model

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.depromeet.housekeeper.util.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class DataStoreManager(context: Context) {
    private val dataStore = context.dataStore

    suspend fun <T> edit (key : Preferences.Key<T>, value: T){
        dataStore.edit{ preferences->
            preferences[key] = value
        }
    }

    suspend fun <T> get (key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }
            .map { prefs ->
                prefs[key] ?: defaultValue
            }
    }
}
