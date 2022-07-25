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
  suspend fun getGoogleLogin(socialType: SocialType): Flow<LoginResponse>
  suspend fun deleteHouseWork(houseWorkId: Int): Flow<Unit>
  suspend fun editHouseWork(houseWorkId: Int, chore: Chore): Flow<HouseWork>
  suspend fun updateChoreState(
    houseWorkId: Int,
    updateChoreBody: UpdateChoreBody,
  ): Flow<UpdateChoreResponse>

  suspend fun logout(): Flow<Unit>
  suspend fun buildTeam(buildTeam: BuildTeam): Flow<BuildTeamResponse>
  suspend fun getTeam(): Flow<Groups>
  suspend fun getProfileImages(): Flow<ProfileImages>
  suspend fun updateMember(updateMember: UpdateMember): Flow<UpdateMemberResponse>
  suspend fun getInviteCode(): Flow<GetInviteCode>
  suspend fun updateTeam(teamName: BuildTeam): Flow<TeamUpdateResponse>
  suspend fun joinTeam(inviteCode: JoinTeam): Flow<JoinTeamResponse>
  suspend fun createRule(rule: Rule): Flow<RuleResponses>
  suspend fun getRules(): Flow<RuleResponses>
  suspend fun deleteRule(ruleId: Int): Flow<Response>
  suspend fun leaveTeam(): Flow<Unit>
  suspend fun getMe(): Flow<ProfileData>
  suspend fun updateMe(editProfileModel: EditProfileModel): Flow<EditResponseBody>
  suspend fun getDetailHouseWorks(houseWorkId: Int): Flow<HouseWork>
  suspend fun saveToken(token: String): Flow<Unit>
}
