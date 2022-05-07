package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.ChorePreset
import com.depromeet.housekeeper.model.CompleteHouseWork
import com.depromeet.housekeeper.model.HouseWorks
import kotlinx.coroutines.flow.Flow


interface RemoteDataSource {
  suspend fun getList(scheduledDate: String): Flow<HouseWorks>
  suspend fun getHouseWorkList(): Flow<ChorePreset>
  suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork>
}