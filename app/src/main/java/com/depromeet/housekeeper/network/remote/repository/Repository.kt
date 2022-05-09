package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.ChoreList
import com.depromeet.housekeeper.model.ChorePreset
import com.depromeet.housekeeper.model.CompleteHouseWork
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.UpdateChoreResponse
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

  override suspend fun updateChoreState(houseWorkId: Int): Flow<UpdateChoreResponse> =
    flow {
      emit(apiService.updateChoreState(houseWorkId))
    }
}
