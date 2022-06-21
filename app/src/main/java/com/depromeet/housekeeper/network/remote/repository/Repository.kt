package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.network.remote.api.RemoteDataSource
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import com.depromeet.housekeeper.network.remote.model.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object Repository : RemoteDataSource {
  private val apiService = RetrofitBuilder.apiService

  override suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse> = flow {
    emit(apiService.createHouseWorks(houseWorks))
  }

  override suspend fun getList(scheduledDate: String): Flow<List<HouseWorks>> = flow {
    emit(apiService.getList(scheduledDate))
  }

  override suspend fun getHouseWorkList(): Flow<ChorePreset> = flow {
    emit(apiService.getChoreList())
  }

  override suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork> =
    flow {
      emit(apiService.getCompletedHouseWorkNumber(scheduledDate))
    }

  override suspend fun getGoogleLogin(
    auth: String,
    socialType: SocialType,
  ): Flow<LoginResponse> = flow {
    emit(apiService.googlelogin(auth, socialType))
  }

  override suspend fun deleteHouseWork(id: Int): Flow<Unit> =
    flow {
      emit(apiService.deleteHouseWork(id))
    }

  override suspend fun editHouseWork(id: Int, chore: Chore): Flow<HouseWork> = flow {
    apiService.editHouseWork(id, chore)
  }

  override suspend fun updateChoreState(
    houseWorkId: Int,
    updateChoreBody: UpdateChoreBody,
  ): Flow<UpdateChoreResponse> =
    flow {
      emit(apiService.updateChoreState(houseWorkId, updateChoreBody))
    }

  override suspend fun logout(
    auth: String
  ): Flow<Unit> = flow {
    emit(apiService.logout(auth))
  }

  override suspend fun buildTeam(
    buildTeam : BuildTeam
  ): Flow<BuildTeamResponse> = flow {
    emit(apiService.buildTeam(buildTeam))
  }

  override suspend fun getTeam(): Flow<Groups> = flow {
    emit(apiService.getTeamData())
  }

  override suspend fun getProfileImages(): Flow<ProfileImages> = flow {
    emit(apiService.getProfileImages())
  }

  override suspend fun updateMember(updateMember: UpdateMember): Flow<UpdateMemberResponse> = flow {
    emit(apiService.updateMember(updateMember = updateMember))
  }
}
