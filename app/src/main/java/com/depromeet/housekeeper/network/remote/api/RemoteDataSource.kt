package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.ChorePreset
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.CompleteHouseWork
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.UpdateChoreBody
import com.depromeet.housekeeper.model.UpdateChoreResponse
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
  suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse>
  suspend fun getList(scheduledDate: String): Flow<HouseWorks>
  suspend fun getHouseWorkList(): Flow<ChorePreset>
  suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork>
  suspend fun updateChoreState(
    houseWorkId: Int,
    updateChoreBody: UpdateChoreBody,
  ): Flow<UpdateChoreResponse>
}
