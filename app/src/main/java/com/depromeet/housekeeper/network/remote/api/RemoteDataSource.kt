package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.HouseWorks
import kotlinx.coroutines.flow.Flow


interface RemoteDataSource{
  suspend fun getList(scheduledDate: String): Flow<List<HouseWorks>>
}