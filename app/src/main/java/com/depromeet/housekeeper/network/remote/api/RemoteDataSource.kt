package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.network.remote.model.SampleResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class RemoteDataSource(
  private val apiService: ApiService,
) {
  suspend fun getList(): Flow<List<SampleResponse>> = flow {
    emit(apiService.getList())
  }
}