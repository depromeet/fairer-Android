package com.depromeet.housekeeper.data.dataSource

import com.depromeet.housekeeper.data.ApiService
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.model.request.*
import com.depromeet.housekeeper.model.response.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

//todo CoroutineDispatcher IO 사용해서 server로부터 정보 받아오도록 변경
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher
) : RemoteDataSource {

    /**
     * houseWorks
     */
    override suspend fun createHouseWorks(houseWorks: Chores): Flow<ApiResult<HouseWorkCreateResponse>> =
        safeFlow {
            apiService.createHouseWorks(houseWorks)
        }.flowOn(ioDispatcher)

    override suspend fun editHouseWork(id: Int, chore: Chore): Flow<HouseWork> = flow {
        emit(apiService.editHouseWork(id, chore))
    }.flowOn(ioDispatcher)

    override suspend fun deleteHouseWork(id: Int): Flow<Unit> =
        flow {
            emit(apiService.deleteHouseWork(id))
        }.flowOn(ioDispatcher)

    override suspend fun updateChoreState(
        houseWorkId: Int,
        updateChoreBody: UpdateChoreBody,
    ): Flow<UpdateChoreResponse> =
        flow {
            emit(apiService.updateChoreState(houseWorkId, updateChoreBody))
        }.flowOn(ioDispatcher)

    override suspend fun getDetailHouseWorks(houseWorkId: Int): Flow<HouseWork> = flow {
        emit(apiService.getDetailHouseWork(houseWorkId))
    }.flowOn(ioDispatcher)

    override suspend fun getDateHouseWorkList(
        fromDate: String,
        toDate: String
    ): Flow<Map<String, HouseWorks>> {
        return flow{
            emit(apiService.getDateHouseWorkList(fromDate, toDate))
        }.flowOn(ioDispatcher)
    }

    override suspend fun getPeriodHouseWorkListOfMember(
        teamMemberId: Int,
        fromDate: String,
        toDate: String
    ): Flow<ApiResult<Map<String, HouseWorks>>> {
        return safeFlow {
            apiService.getPeriodHouseWorkListOfMember(teamMemberId, fromDate, toDate)
        }.flowOn(ioDispatcher)
    }

    override suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<ApiResult<CompleteHouseWork>> =
        safeFlow {
            apiService.getCompletedHouseWorkNumber(scheduledDate)
        }.flowOn(ioDispatcher)

    /**
     * members
     */
    override suspend fun getProfileImages(): Flow<ProfileImages> = flow {
        emit(apiService.getProfileImages())
    }.flowOn(ioDispatcher)

    override suspend fun updateMember(updateMember: UpdateMember): Flow<UpdateMemberResponse> =
        flow {
            emit(apiService.updateMember(updateMember = updateMember))
        }.flowOn(ioDispatcher)

    override suspend fun getMe(): Flow<ProfileData> = flow {
        emit(apiService.getMe())
    }.flowOn(ioDispatcher)

    override suspend fun updateMe(editProfileModel: EditProfileModel): Flow<EditResponseBody> =
        flow {
            emit(apiService.updateMe(editProfileModel))
        }.flowOn(ioDispatcher)


    /**
     * oauth
     */
    override suspend fun getGoogleLogin(
        socialType: SocialType,
    ): Flow<LoginResponse> = flow {
        emit(apiService.googlelogin(socialType))
    }.flowOn(ioDispatcher)

    override suspend fun logout(
    ): Flow<Unit> = flow {
        emit(apiService.logout())
    }.flowOn(ioDispatcher)


    /**
     * presets
     */
    override suspend fun getHouseWorkList(): Flow<List<ChoreList>> = flow {
        emit(apiService.getChoreList())
    }.flowOn(ioDispatcher)


    /**
     * rules
     */
    override suspend fun createRule(rule: Rule): Flow<ApiResult<RuleResponses>> = safeFlow {
        apiService.createRules(rule)
    }.flowOn(ioDispatcher)

    override suspend fun getRules(): Flow<ApiResult<RuleResponses>> = safeFlow {
        apiService.getRules()
    }.flowOn(ioDispatcher)

    override suspend fun deleteRule(ruleId: Int): Flow<ApiResult<Response>> = safeFlow {
        apiService.deleteRule(ruleId)
    }.flowOn(ioDispatcher)


    /**
     * teams
     */
    override suspend fun buildTeam(
        buildTeam: BuildTeam,
    ): Flow<BuildTeamResponse> = flow {
        emit(apiService.buildTeam(buildTeam))
    }.flowOn(ioDispatcher)

    override suspend fun getTeam(): Flow<Groups> = flow {
        emit(apiService.getTeamData())
    }.flowOn(ioDispatcher)

    override suspend fun getInviteCode(): Flow<GetInviteCode> = flow {
        emit(apiService.getInviteCode())
    }.flowOn(ioDispatcher)

    override suspend fun updateTeam(teamName: BuildTeam): Flow<TeamUpdateResponse> = flow {
        emit(apiService.updateTeam(teamName))
    }.flowOn(ioDispatcher)

    override suspend fun joinTeam(inviteCode: JoinTeam): Flow<JoinTeamResponse> = flow {
        emit(apiService.joinTeam(inviteCode))
    }.flowOn(ioDispatcher)

    override suspend fun leaveTeam(): Flow<Unit> = flow {
        emit(apiService.leaveTeam())
    }.flowOn(ioDispatcher)


    /**
    fcm
     */
    override suspend fun saveToken(token: Token): Flow<Unit> = flow {
        emit(apiService.saveToken(token = token))
    }.flowOn(ioDispatcher)

    override suspend fun sendMessage(message: Message): Flow<Message> = flow {
        emit(apiService.sendMessage(message = message))
    }.flowOn(ioDispatcher)

}