package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.ChoreList
import com.depromeet.housekeeper.model.HouseWorks
import kotlinx.coroutines.flow.Flow


interface RemoteDataSource{
  suspend fun getList(scheduledDate: String): Flow<HouseWorks>
  suspend fun getHouseWorkList(space : String): Flow<ChoreList>
}