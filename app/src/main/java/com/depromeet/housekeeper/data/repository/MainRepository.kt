package com.depromeet.housekeeper.data.repository

import com.depromeet.housekeeper.data.dataSource.RemoteDataSourceImpl
import com.depromeet.housekeeper.model.request.*
import com.depromeet.housekeeper.model.response.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// mutex 사용할 일 있으면 여기서
// externalScope: 취소되면 안되는 작업
// repository에서 mapper 사용해 실제 사용하는 데이터로 바꾸어줌
class MainRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val externalScope: CoroutineScope? = null
) {
    /**
     * houseWorks
     */
    suspend fun createHouseWorks(houseWorks: List<Chore>): Flow<ApiResult<List<HouseWork>>> =
        remoteDataSource.createHouseWorks(houseWorks)

    suspend fun updateChoreState(
        houseWorkId: Int,
        scheduledDate: String,
    ): Flow<UpdateChoreResponse> = remoteDataSource.updateChoreState(houseWorkId,scheduledDate)

    suspend fun updateChoreComplete(
        houseWorkId: Int
    )=remoteDataSource.updateChoreComplete(houseWorkId)

    suspend fun getDetailHouseWorks(houseWorkId: Int): Flow<ApiResult<HouseWork>> =
        remoteDataSource.getDetailHouseWorks(houseWorkId)

    suspend fun getPeriodHouseWorkListOfMember(
        teamMemberId: Int,
        fromDate: String,
        toDate: String
    ): Flow<ApiResult<Map<String, HouseWorks>>> = remoteDataSource.getPeriodHouseWorkListOfMember(teamMemberId, fromDate, toDate)

    suspend fun getDateHouseWorkList(
        fromDate: String,
        toDate: String
    ): Flow<ApiResult<Map<String, HouseWorks>>> = remoteDataSource.getDateHouseWorkList(fromDate, toDate)

    suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<ApiResult<CompleteHouseWork>> =
        remoteDataSource.getCompletedHouseWorkNumber(scheduledDate)

    suspend fun editHouseWork(editChore: EditChore):  Flow<ApiResult<Unit>> =
        remoteDataSource.editHouseWork(editChore)

    suspend fun deleteHouseWork(deleteChore: DeleteChoreRequest): Flow<ApiResult<Unit>> =
        remoteDataSource.deleteHouseWork(deleteChore)


    /**
     * presets
     */
    suspend fun getHouseWorkList(): Flow<ApiResult<List<ChoreList>>> =
        remoteDataSource.getHouseWorkList()

    /**
     * rules
     */
    suspend fun createRule(rule: Rule): Flow<ApiResult<RuleResponses>> =
        remoteDataSource.createRule(rule)
    suspend fun getRules(): Flow<ApiResult<RuleResponses>> =
        remoteDataSource.getRules()
    suspend fun deleteRule(ruleId: Int): Flow<ApiResult<Response>> =
        remoteDataSource.deleteRule(ruleId)

}