package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.network.remote.api.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


object Repository : RemoteDataSource {
  private val ApiService = RetrofitBuilder.apiService
//  override suspend fun getList(scheduledDate: String): Flow<HouseWorks> = flow {
//    ApiService.getList(scheduledDate)
//  }

  override suspend fun createHouseWorks(houseWorks: List<Chore>): Flow<HouseWork> = flow {
    ApiService.createHouseWorks(houseWorks)
  }
}