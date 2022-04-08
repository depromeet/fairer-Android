package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.network.remote.api.RemoteDataSource
import com.depromeet.housekeeper.network.remote.model.SampleResponse
import kotlinx.coroutines.flow.Flow

class Repository(private val remoteDataSource: RemoteDataSource) {
  suspend fun getList(): Flow<List<SampleResponse>> = remoteDataSource.getList()

}