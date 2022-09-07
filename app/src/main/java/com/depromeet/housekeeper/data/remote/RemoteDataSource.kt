package com.depromeet.housekeeper.data.remote

import com.depromeet.housekeeper.model.*
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    /**
    fcm
     */
    suspend fun saveToken(token: Token): Flow<Unit>
    suspend fun sendMessage(message: Message): Flow<Message>

    /**
     * houseWorks
     */
    suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse>
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
    ): Flow<Map<String, HouseWorks>>

    suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork>

    suspend fun getList(scheduledDate: String): Flow<List<HouseWorks>>

    /**
     * members
     */
    suspend fun getProfileImages(): Flow<ProfileImages>
    suspend fun updateMember(updateMember: UpdateMember): Flow<UpdateMemberResponse>
    suspend fun getMe(): Flow<ProfileData>
    suspend fun updateMe(editProfileModel: EditProfileModel): Flow<EditResponseBody>

    /**
     * oauth
     */
    suspend fun getGoogleLogin(socialType: SocialType): Flow<LoginResponse>
    suspend fun logout(): Flow<Unit>

    /**
     * presets
     */
    suspend fun getHouseWorkList(): Flow<List<ChoreList>>

    /**
     * rules
     */
    suspend fun createRule(rule: Rule): Flow<RuleResponses>
    suspend fun getRules(): Flow<RuleResponses>
    suspend fun deleteRule(ruleId: Int): Flow<Response>

    /**
     * teams
     */
    suspend fun buildTeam(buildTeam: BuildTeam): Flow<BuildTeamResponse>
    suspend fun getTeam(): Flow<Groups>
    suspend fun getInviteCode(): Flow<GetInviteCode>
    suspend fun updateTeam(teamName: BuildTeam): Flow<TeamUpdateResponse>
    suspend fun joinTeam(inviteCode: JoinTeam): Flow<JoinTeamResponse>
    suspend fun leaveTeam(): Flow<Unit>

}
