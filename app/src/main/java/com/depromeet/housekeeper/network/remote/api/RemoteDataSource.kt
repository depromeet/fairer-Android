package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.AddHouseWorks
import kotlinx.coroutines.flow.Flow


interface RemoteDataSource{
  suspend fun getList(scheduledDate: String): Flow<HouseWorks>

  suspend fun createHouseWorks(houseWorks: AddHouseWorks): Flow<HouseWorks>
}