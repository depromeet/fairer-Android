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
  suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse>
  suspend fun getList(scheduledDate: String): Flow<HouseWorks>
  suspend fun getHouseWorkList(): Flow<ChorePreset>
  suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork>
  suspend fun deleteHouseWork(id: Int)
  suspend fun editHouseWork(id: Int, chore: Chore): Flow<HouseWork>
  suspend fun updateChoreState(
    houseWorkId: Int,
    updateChoreBody: UpdateChoreBody,
  ): Flow<UpdateChoreResponse>
}
