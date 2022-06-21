package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import com.depromeet.housekeeper.network.remote.model.LoginResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
  suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse>
  suspend fun getList(scheduledDate: String): Flow<List<HouseWorks>>
  suspend fun getHouseWorkList(): Flow<ChorePreset>
  suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork>
  suspend fun getGoogleLogin(auth: String, socialType: SocialType): Flow<LoginResponse>
  suspend fun deleteHouseWork(id: Int): Flow<Unit>
  suspend fun editHouseWork(id: Int, chore: Chore): Flow<HouseWork>
  suspend fun updateChoreState(
    houseWorkId: Int,
    updateChoreBody: UpdateChoreBody,
  ): Flow<UpdateChoreResponse>
  suspend fun logout(auth: String): Flow<Unit>
  suspend fun buildTeam(teamName : String) : Flow<BuildTeam>
  suspend fun getTeam(): Flow<Groups>
  suspend fun getProfileImages(): Flow<ProfileImages>
}
