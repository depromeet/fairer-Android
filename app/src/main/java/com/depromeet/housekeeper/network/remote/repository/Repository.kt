package com.depromeet.housekeeper.network.remote.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.network.remote.api.RemoteDataSource
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import com.depromeet.housekeeper.network.remote.model.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object Repository : RemoteDataSource {
  private val apiService = RetrofitBuilder.apiService
  val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "loginToken")

  override suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse> = flow {
    apiService.createHouseWorks(houseWorks)
  }
  override suspend fun getList(scheduledDate: String): Flow<HouseWorks> = flow {
    apiService.getList(scheduledDate)
  }

  override suspend fun getHouseWorkList(): Flow<ChorePreset> = flow {
    emit(apiService.getChoreList())
  }

  override suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork> =
    flow {
      emit(apiService.getCompletedHouseWorkNumber(scheduledDate))
    }

  override suspend fun getGoogleLogin(auth: String, socialType: SocialType): Flow<LoginResponse> = flow {
    emit(apiService.googlelogin(auth, socialType))
  }
}
