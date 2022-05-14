package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.*
import kotlinx.coroutines.flow.Flow
import com.depromeet.housekeeper.model.ChorePreset
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.CompleteHouseWork
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.UpdateChoreBody
import com.depromeet.housekeeper.model.UpdateChoreResponse
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse

interface RemoteDataSource {
  suspend fun createHouseWorks(authorization:String, houseWorks: Chores): Flow<HouseWorkCreateResponse>
  suspend fun getList(authorization:String, scheduledDate: String): Flow<HouseWorks>
  suspend fun getHouseWorkList(authorization:String): Flow<ChorePreset>
  suspend fun getCompletedHouseWorkNumber(authorization:String, scheduledDate: String): Flow<CompleteHouseWork>
  suspend fun deleteHouseWork(authorization:String, id: Int)
  suspend fun editHouseWork(authorization:String, id: Int, chore: Chore): Flow<HouseWork>
  suspend fun updateChoreState(
    authorization:String,
    houseWorkId: Int,
    updateChoreBody: UpdateChoreBody,
  ): Flow<UpdateChoreResponse>
}
