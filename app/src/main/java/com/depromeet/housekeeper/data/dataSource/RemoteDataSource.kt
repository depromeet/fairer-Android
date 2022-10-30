package com.depromeet.housekeeper.data.dataSource

import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.model.request.*
import com.depromeet.housekeeper.model.response.*
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    /**
     * houseWorks
     */
    suspend fun createHouseWorks(houseWorks: List<Chore>): Flow<ApiResult<List<HouseWork>>>
    suspend fun editHouseWork(houseWorkId: Int, chore: Chore): Flow<HouseWork>
    suspend fun deleteHouseWork(houseWorkId: Int): Flow<Unit>
    suspend fun updateChoreState(
        houseWorkId: Int,
        updateChoreBody: UpdateChoreBody,
    ): Flow<UpdateChoreResponse>

    suspend fun getDetailHouseWorks(houseWorkId: Int): Flow<HouseWork>
    suspend fun getDateHouseWorkList(
        fromDate: String,
        toDate: String
    ): Flow<Map<String, HouseWorks>>

    suspend fun getPeriodHouseWorkListOfMember(
        teamMemberId: Int,
        fromDate: String,
        toDate: String
    ): Flow<ApiResult<Map<String, HouseWorks>>>

    suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<ApiResult<CompleteHouseWork>>

    /**
     * members
     */
    suspend fun getProfileImages(): Flow<ApiResult<ProfileImages>>
    suspend fun updateMember(updateMember: UpdateMember): Flow<ApiResult<UpdateMemberResponse>>
    suspend fun getMe(): Flow<ApiResult<ProfileData>>
    suspend fun updateMe(editProfileModel: EditProfileModel): Flow<ApiResult<EditResponseBody>>

    /**
     * oauth
     */
    suspend fun getGoogleLogin(socialType: SocialType): Flow<ApiResult<LoginResponse>>
    suspend fun logout(): Flow<ApiResult<Unit>>

    /**
     * presets
     */
    suspend fun getHouseWorkList(): Flow<ApiResult<List<ChoreList>>>

    /**
     * rules
     */
    suspend fun createRule(rule: Rule): Flow<ApiResult<RuleResponses>>
    suspend fun getRules(): Flow<ApiResult<RuleResponses>>
    suspend fun deleteRule(ruleId: Int): Flow<ApiResult<Response>>

    /**
     * teams
     */
    suspend fun buildTeam(buildTeam: BuildTeam): Flow<ApiResult<BuildTeamResponse>>
    suspend fun getTeam(): Flow<ApiResult<Groups>>
    suspend fun getInviteCode(): Flow<ApiResult<GetInviteCode>>
    suspend fun updateTeam(teamName: BuildTeam): Flow<ApiResult<TeamUpdateResponse>>
    suspend fun joinTeam(inviteCode: JoinTeam): Flow<ApiResult<JoinTeamResponse>>
    suspend fun leaveTeam(): Flow<ApiResult<Unit>>

    /**
    fcm
     */
    suspend fun saveToken(token: Token): Flow<ApiResult<Unit>>
    suspend fun sendMessage(message: Message): Flow<ApiResult<Message>>

}
