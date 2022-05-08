package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.*
import kotlinx.coroutines.flow.Flow
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import com.depromeet.housekeeper.network.remote.model.LoginResponse

interface RemoteDataSource {
  suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse>
  suspend fun getList(scheduledDate: String): Flow<HouseWorks>
  suspend fun getHouseWorkList(): Flow<ChorePreset>
  suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork>
  suspend fun getGoogleLogin(auth: String, socialType: SocialType): Flow<LoginResponse>
}
