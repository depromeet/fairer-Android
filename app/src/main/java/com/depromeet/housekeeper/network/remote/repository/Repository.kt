package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.network.remote.api.RemoteDataSource
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object Repository : RemoteDataSource {
  private val apiService = RetrofitBuilder.apiService

  override suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse> = flow {
    apiService.createHouseWorks(houseWorks)
  }
  override suspend fun getList(scheduledDate: String): Flow<HouseWorks> = flow {
    emit(apiService.getList(scheduledDate))
  }

  override suspend fun getHouseWorkList(): Flow<ChorePreset> = flow {
    emit(apiService.getChoreList())
  }

  override suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork> =
    flow {
      emit(apiService.getCompletedHouseWorkNumber(scheduledDate))
    }

  override suspend fun deleteHouseWork(id: Int) {
    apiService.deleteHouseWork(id)
  }

  override suspend fun editHouseWork(id: Int, chore: Chore): Flow<HouseWork> = flow {
    apiService.editHouseWork(id, chore)
  }
}
