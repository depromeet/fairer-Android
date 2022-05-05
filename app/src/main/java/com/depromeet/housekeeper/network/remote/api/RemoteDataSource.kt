package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.Chores
import kotlinx.coroutines.flow.Flow
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse

interface RemoteDataSource {
  suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse>
  suspend fun getList(scheduledDate: String): Flow<HouseWorks>
}

