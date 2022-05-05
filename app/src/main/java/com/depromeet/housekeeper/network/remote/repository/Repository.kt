package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.AddHouseWorks
import com.depromeet.housekeeper.network.remote.api.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object Repository : RemoteDataSource {
  private val apiService = RetrofitBuilder.apiService
  override suspend fun getList(scheduledDate: String): Flow<HouseWorks> = flow {
    apiService.getList(scheduledDate)
  }

  override suspend fun createHouseWorks(houseWorks: AddHouseWorks): Flow<HouseWorks> = flow {
    apiService.createHouseWorks(houseWorks)
  }
}
