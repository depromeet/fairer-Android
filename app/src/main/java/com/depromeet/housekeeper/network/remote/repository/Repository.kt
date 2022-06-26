package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.model.BuildTeam
import com.depromeet.housekeeper.model.BuildTeamResponse
import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.model.ChorePreset
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.CompleteHouseWork
import com.depromeet.housekeeper.model.GetInviteCode
import com.depromeet.housekeeper.model.Groups
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.JoinTeam
import com.depromeet.housekeeper.model.JoinTeamResponse
import com.depromeet.housekeeper.model.ProfileData
import com.depromeet.housekeeper.model.ProfileImages
import com.depromeet.housekeeper.model.Response
import com.depromeet.housekeeper.model.Rule
import com.depromeet.housekeeper.model.RuleResponses
import com.depromeet.housekeeper.model.SocialType
import com.depromeet.housekeeper.model.TeamUpdateResponse
import com.depromeet.housekeeper.model.UpdateChoreBody
import com.depromeet.housekeeper.model.UpdateChoreResponse
import com.depromeet.housekeeper.model.UpdateMember
import com.depromeet.housekeeper.model.UpdateMemberResponse
import com.depromeet.housekeeper.network.remote.api.RemoteDataSource
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import com.depromeet.housekeeper.network.remote.model.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object Repository : RemoteDataSource  {
  private val apiService = RetrofitBuilder.apiService

  override suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse> =
    flow {
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
    socialType: SocialType,
  ): Flow<LoginResponse> = flow {
    emit(apiService.googlelogin(socialType))
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
  ): Flow<Unit> = flow {
    emit(apiService.logout())
  }

  override suspend fun buildTeam(
    buildTeam: BuildTeam,
  ): Flow<BuildTeamResponse> = flow {
    emit(apiService.buildTeam(buildTeam))
  }

  override suspend fun getTeam(): Flow<Groups> = flow {
    emit(apiService.getTeamData())
  }

  override suspend fun getProfileImages(): Flow<ProfileImages> = flow {
    emit(apiService.getProfileImages())
  }

  override suspend fun updateMember(updateMember: UpdateMember): Flow<UpdateMemberResponse> =
    flow {
      emit(apiService.updateMember(updateMember = updateMember))
    }

  override suspend fun getInviteCode(): Flow<GetInviteCode> = flow {
    emit(apiService.getInviteCode())
  }

  override suspend fun updateTeam(teamName: BuildTeam): Flow<TeamUpdateResponse> = flow {
    emit(apiService.updateTeam(teamName))
  }

  override suspend fun joinTeam(inviteCode: JoinTeam): Flow<JoinTeamResponse> = flow {
    emit(apiService.joinTeam(inviteCode))
  }


  override suspend fun createRule(rule: Rule): Flow<RuleResponses> = flow {
    emit(apiService.createRules(rule))
  }

  override suspend fun getRules(): Flow<RuleResponses> = flow {
    emit(apiService.getRules())
  }

  override suspend fun deleteRule(ruleId: Int): Flow<Response> = flow {
    emit(apiService.deleteRule(ruleId))
  }

  override suspend fun leaveTeam(): Flow<Unit> = flow {
    emit(apiService.leaveTeam())
  }

  override suspend fun getMe(): Flow<ProfileData> = flow {
    emit(apiService.getMe())
  }

  override suspend fun updateMe(
    memberName: String,
    profilePath: String,
    statueMessage: String,
  ): Flow<ProfileData> = flow {
    emit(apiService.updateMe(memberName, profilePath, statueMessage))
  }

  override suspend fun getDetailHouseWorks(houseWorkId: Int): Flow<HouseWork> = flow {
    emit(apiService.getDetailHouseWork(houseWorkId))
  }
}
