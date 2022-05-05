package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.model.HouseWork
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
  suspend fun createHouseWorks(houseWorks: List<Chore>): Flow<List<HouseWork>>
}
