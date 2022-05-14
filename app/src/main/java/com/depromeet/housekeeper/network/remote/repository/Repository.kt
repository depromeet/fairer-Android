package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.ChorePreset
import com.depromeet.housekeeper.model.CompleteHouseWork
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.UpdateChoreBody
import com.depromeet.housekeeper.model.UpdateChoreResponse
import com.depromeet.housekeeper.network.remote.api.RemoteDataSource
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object Repository : RemoteDataSource {
  private val apiService = RetrofitBuilder.apiService

  override suspend fun createHouseWorks(authorization: String, houseWorks: Chores): Flow<HouseWorkCreateResponse> = flow {
    apiService.createHouseWorks(authorization, houseWorks)
  }

  override suspend fun getList(authorization:String, scheduledDate: String): Flow<HouseWorks> = flow {
    emit(apiService.getList(authorization, scheduledDate))
  }

  override suspend fun getHouseWorkList(authorization:String): Flow<ChorePreset> = flow {
    emit(apiService.getChoreList(authorization))
  }

  override suspend fun getCompletedHouseWorkNumber(authorization: String, scheduledDate: String): Flow<CompleteHouseWork> =
    flow {
      emit(apiService.getCompletedHouseWorkNumber(authorization, scheduledDate))
    }

  override suspend fun deleteHouseWork(authorization: String, id: Int) {
    apiService.deleteHouseWork(authorization, id)
  }

  override suspend fun editHouseWork(authorization: String, id: Int, chore: Chore): Flow<HouseWork> = flow {
    apiService.editHouseWork(authorization, id, chore)
  }

  override suspend fun updateChoreState(
    authorization:String,
    houseWorkId: Int,
    updateChoreBody: UpdateChoreBody,
  ): Flow<UpdateChoreResponse> =
    flow {
      emit(apiService.updateChoreState(authorization, houseWorkId, updateChoreBody))
    }
}
