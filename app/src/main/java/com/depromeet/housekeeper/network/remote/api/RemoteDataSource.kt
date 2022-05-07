package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.ChoreList
import com.depromeet.housekeeper.model.CompleteHouseWork
import com.depromeet.housekeeper.model.HouseWorks
import kotlinx.coroutines.flow.Flow
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse

interface RemoteDataSource {
  suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse>
  suspend fun getList(scheduledDate: String): Flow<HouseWorks>
  suspend fun getHouseWorkList(space : String): Flow<ChoreList>
  suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork>
}
