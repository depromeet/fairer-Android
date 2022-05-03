package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.network.remote.api.ApiService
import com.depromeet.housekeeper.network.remote.api.RemoteDataSource
import com.depromeet.housekeeper.network.remote.model.SampleResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object Repository : RemoteDataSource {
  private val ApiService = RetrofitBuilder.apiService
  override suspend fun getList(scheduledDate: String): Flow<List<HouseWorks>> = flow {
    ApiService.getList(scheduledDate)
  }
}
