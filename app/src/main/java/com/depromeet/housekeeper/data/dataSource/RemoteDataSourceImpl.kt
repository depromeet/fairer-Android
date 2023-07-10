package com.depromeet.housekeeper.data.dataSource

import com.depromeet.housekeeper.data.ApiService
import com.depromeet.housekeeper.model.request.*
import com.depromeet.housekeeper.model.response.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher
) : RemoteDataSource {

    /**
     * houseWorks Complete
     */
    override suspend fun updateChoreState(
        houseWorkId: Int,
        scheduledDate: String,
    ): Flow<ApiResult<UpdateChoreResponse>> =
        safeFlow {
            apiService.updateChoreState(houseWorkId, scheduledDate)
        }.flowOn(ioDispatcher)


    override suspend fun deleteChoreComplete(houseWorkId: Int): Flow<ApiResult<Unit>> =
        safeFlow {
            apiService.deleteChoreComplete(houseWorkId)
        }.flowOn(ioDispatcher)

    /**
     * houseWorks
     */
    override suspend fun createHouseWorks(houseWorks: List<Chore>): Flow<ApiResult<List<HouseWork>>> =
        safeFlow {
            apiService.createHouseWorks(houseWorks)
        }.flowOn(ioDispatcher)

    override suspend fun getDetailHouseWorks(houseWorkId: Int): Flow<ApiResult<HouseWork>> =
        safeFlow {
            apiService.getDetailHouseWork(houseWorkId)
        }.flowOn(ioDispatcher)

    override suspend fun getDateHouseWorkList(
        fromDate: String,
        toDate: String
    ): Flow<ApiResult<Map<String, HouseWorks>>> {
        return safeFlow {
            apiService.getDateHouseWorkList(fromDate, toDate)
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

    override suspend fun editHouseWork(editChore: EditChore): Flow<ApiResult<Unit>> = safeFlow {
        apiService.editHouseWork(editChore)
    }.flowOn(ioDispatcher)

    override suspend fun deleteHouseWork(deleteChore: DeleteChoreRequest): Flow<ApiResult<Unit>> =
        safeFlow {
            apiService.deleteHouseWork(deleteChore)
        }.flowOn(ioDispatcher)

    /**
     * members
     */
    override suspend fun getProfileImages(): Flow<ApiResult<ProfileImages>> = safeFlow {
        apiService.getProfileImages()
    }.flowOn(ioDispatcher)

    override suspend fun updateMember(updateMember: UpdateMember): Flow<ApiResult<UpdateMemberResponse>> =
        safeFlow {
            apiService.updateMember(updateMember = updateMember)
        }.flowOn(ioDispatcher)

    override suspend fun getMe(): Flow<ApiResult<ProfileData>> = safeFlow {
        apiService.getMe()
    }.flowOn(ioDispatcher)

    override suspend fun updateMe(editProfileModel: EditProfileModel): Flow<ApiResult<EditResponseBody>> =
        safeFlow {
            apiService.updateMe(editProfileModel)
        }.flowOn(ioDispatcher)


    /**
     * oauth
     */
    override suspend fun getGoogleLogin(
        socialType: SocialType,
    ): Flow<ApiResult<LoginResponse>> = safeFlow {
        apiService.googlelogin(socialType)
    }.flowOn(ioDispatcher)

    override suspend fun logout(
    ): Flow<ApiResult<Unit>> = safeFlow {
        apiService.logout()
    }.flowOn(ioDispatcher)


    /**
     * presets
     */
    override suspend fun getHouseWorkList(): Flow<ApiResult<List<ChoreList>>> = safeFlow {
        apiService.getChoreList()
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
     * statistics
     */
    override suspend fun getStatsList(yearMonth: String): Flow<ApiResult<StatsListResponse>> =
        safeFlow {
            apiService.getStatisticsList(yearMonth)
        }.flowOn(ioDispatcher)

    override suspend fun getStatsRanking(yearMonth: String): Flow<ApiResult<HouseWorkStatsResponse>> =
        safeFlow {
            apiService.getStatisticsLanking(yearMonth)
        }.flowOn(ioDispatcher)

    override suspend fun getHouseWorkStats(
        houseWorkName: String,
        month: String
    ): Flow<ApiResult<HouseWorkStatsResponse>> = safeFlow {
        apiService.getHouseWorkStatistics(houseWorkName, month)
    }.flowOn(ioDispatcher)


    /**
     * teams
     */
    override suspend fun buildTeam(
        buildTeam: BuildTeam,
    ): Flow<ApiResult<BuildTeamResponse>> = safeFlow {
        apiService.buildTeam(buildTeam)
    }.flowOn(ioDispatcher)

    override suspend fun getTeam(): Flow<ApiResult<Groups>> = safeFlow {
        apiService.getTeamData()
    }.flowOn(ioDispatcher)

    override suspend fun getInviteCode(): Flow<ApiResult<GetInviteCode>> = safeFlow {
        apiService.getInviteCode()
    }.flowOn(ioDispatcher)

    override suspend fun updateTeam(teamName: BuildTeam): Flow<ApiResult<TeamUpdateResponse>> =
        safeFlow {
            apiService.updateTeam(teamName)
        }.flowOn(ioDispatcher)

    override suspend fun joinTeam(inviteCode: JoinTeam): Flow<ApiResult<JoinTeamResponse>> =
        safeFlow {
            apiService.joinTeam(inviteCode)
        }.flowOn(ioDispatcher)

    override suspend fun leaveTeam(): Flow<ApiResult<Unit>> = safeFlow {
        apiService.leaveTeam()
    }.flowOn(ioDispatcher)


    /**
    fcm
     */
    override suspend fun saveToken(token: Token): Flow<ApiResult<Unit>> = safeFlow {
        apiService.saveToken(token = token)
    }.flowOn(ioDispatcher)

    override suspend fun sendMessage(message: Message): Flow<ApiResult<Message>> = safeFlow {
        apiService.sendMessage(message = message)
    }.flowOn(ioDispatcher)

    override suspend fun getAlarmInfo(): Flow<ApiResult<Alarm>> = safeFlow {
        apiService.getAlarmInfo()
    }.flowOn(ioDispatcher)

    override suspend fun setAlarm(alarm: Alarm): Flow<ApiResult<Unit>> = safeFlow {
        apiService.setAlarm(alarm)
    }.flowOn(ioDispatcher)
    /**
    feedback
     */
    override suspend fun createFeedback(feedbackModel: CreateFeedbackModel): Flow<ApiResult<Unit>> =safeFlow {
        apiService.createFeedback(feedbackModel =  feedbackModel)
    }.flowOn(ioDispatcher)

    override suspend fun updateFeedback(houseworkCompleteId: Int,comment:String): Flow<ApiResult<Unit>> = safeFlow{
        apiService.updateFeedback(houseworkCompleteId = houseworkCompleteId,comment=Comment(comment))
    }.flowOn(ioDispatcher)

    override suspend fun getFeedbackList(houseWorkCompleteId: Int): Flow<ApiResult<FeedbackListModel>> = safeFlow{
        apiService.getFeedbackList(houseWorkCompleteId = houseWorkCompleteId)
    }.flowOn(ioDispatcher)

    override suspend fun urgeHousework(urgeModel: UrgeModel): Flow<ApiResult<Unit>> = safeFlow{
        apiService.urgeHousework(urgeModel)
    }.flowOn(ioDispatcher)

    override suspend fun deleteFeedback(feedbackId:Int): Flow<ApiResult<Unit>> = safeFlow{
        apiService.deleteFeedback(feedbackId)
    }.flowOn(ioDispatcher)
}
