package com.depromeet.housekeeper.data.repository

import com.depromeet.housekeeper.data.remote.RemoteDataSource
import com.depromeet.housekeeper.model.request.*
import com.depromeet.housekeeper.model.response.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

// mutex 사용할 일 있으면 여기서
// todo CoroutineScope Default로 받아와 결과 캐시 작업
// todo repository 쪼개기
class RemoteRepository(
    private val remoteDataSource: RemoteDataSource,
    private val externalScope: CoroutineScope
) {
    suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse> {
        return remoteDataSource.createHouseWorks(houseWorks)
    }

    suspend fun getList(scheduledDate: String): Flow<List<HouseWorks>> {
        return remoteDataSource.getList(scheduledDate)
    }

    suspend fun getHouseWorkList(): Flow<List<ChoreList>> {
        return remoteDataSource.getHouseWorkList()
    }

    suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork> {
        return remoteDataSource.getCompletedHouseWorkNumber(scheduledDate)
    }

    suspend fun getGoogleLogin(
        socialType: SocialType,
    ): Flow<LoginResponse> {
        return remoteDataSource.getGoogleLogin(socialType)
    }

    suspend fun deleteHouseWork(id: Int): Flow<Unit> {
        return remoteDataSource.deleteHouseWork(id)
    }

    suspend fun editHouseWork(id: Int, chore: Chore): Flow<HouseWork> {
        return remoteDataSource.editHouseWork(id, chore)
    }

    suspend fun updateChoreState(
        houseWorkId: Int,
        updateChoreBody: UpdateChoreBody,
    ): Flow<UpdateChoreResponse> {
        return remoteDataSource.updateChoreState(houseWorkId, updateChoreBody)
    }

    suspend fun logout(
    ): Flow<Unit> {
        return remoteDataSource.logout()
    }

    suspend fun buildTeam(
        buildTeam: BuildTeam,
    ): Flow<BuildTeamResponse> {
        return remoteDataSource.buildTeam(buildTeam)
    }

    suspend fun getTeam(): Flow<Groups> {
        return remoteDataSource.getTeam()
    }

    suspend fun getProfileImages(): Flow<ProfileImages> {
        return remoteDataSource.getProfileImages()
    }

    suspend fun updateMember(updateMember: UpdateMember): Flow<UpdateMemberResponse> {
        return remoteDataSource.updateMember(updateMember = updateMember)
    }

    suspend fun getInviteCode(): Flow<GetInviteCode> {
        return remoteDataSource.getInviteCode()
    }

    suspend fun updateTeam(teamName: BuildTeam): Flow<TeamUpdateResponse> {
        return remoteDataSource.updateTeam(teamName)
    }

    suspend fun joinTeam(inviteCode: JoinTeam): Flow<JoinTeamResponse> {
        return remoteDataSource.joinTeam(inviteCode)
    }


    suspend fun createRule(rule: Rule): Flow<RuleResponses> {
        return remoteDataSource.createRule(rule)
    }

    suspend fun getRules(): Flow<RuleResponses> {
        return remoteDataSource.getRules()
    }

    suspend fun deleteRule(ruleId: Int): Flow<Response> {
        return remoteDataSource.deleteRule(ruleId)
    }

    suspend fun leaveTeam(): Flow<Unit> {
        return remoteDataSource.leaveTeam()
    }

    suspend fun getMe(): Flow<ProfileData> {
        return remoteDataSource.getMe()
    }

    suspend fun updateMe(editProfileModel: EditProfileModel): Flow<EditResponseBody> {
        return remoteDataSource.updateMe(editProfileModel)
    }

    suspend fun getDetailHouseWorks(houseWorkId: Int): Flow<HouseWork> {
        return remoteDataSource.getDetailHouseWorks(houseWorkId)
    }

    suspend fun getDateHouseWorkList(
        fromDate: String,
        toDate: String
    ): Flow<Map<String, HouseWorks>> {

        return remoteDataSource.getDateHouseWorkList(fromDate, toDate)

    }

    suspend fun getPeriodHouseWorkListOfMember(
        teamMemberId: Int,
        fromDate: String,
        toDate: String
    ): Flow<Map<String, HouseWorks>> {
        return remoteDataSource.getPeriodHouseWorkListOfMember(
            teamMemberId,
            fromDate,
            toDate
        )
    }

    suspend fun saveToken(token: Token): Flow<Unit> {
        return remoteDataSource.saveToken(token = token)
    }

    suspend fun sendMessage(message: Message): Flow<Message> {
        return remoteDataSource.sendMessage(message = message)
    }


}