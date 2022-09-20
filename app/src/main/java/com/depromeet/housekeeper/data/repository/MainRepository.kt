package com.depromeet.housekeeper.data.repository


import com.depromeet.housekeeper.data.dataSource.RemoteDataSourceImpl
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.model.request.*
import com.depromeet.housekeeper.model.response.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// mutex 사용할 일 있으면 여기서
// externalScope: 취소되면 안되는 작업
class MainRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val externalScope: CoroutineScope? = null
) {
    /**
     * houseWorks
     */
    suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse> =
        remoteDataSource.createHouseWorks(houseWorks)

    suspend fun editHouseWork(houseWorkId: Int, chore: Chore): Flow<HouseWork> =
        remoteDataSource.editHouseWork(houseWorkId,chore)

    suspend fun deleteHouseWork(houseWorkId: Int): Flow<Unit> =
        remoteDataSource.deleteHouseWork(houseWorkId)

    suspend fun updateChoreState(
        houseWorkId: Int,
        updateChoreBody: UpdateChoreBody,
    ): Flow<UpdateChoreResponse> = remoteDataSource.updateChoreState(houseWorkId,updateChoreBody)

    suspend fun getDetailHouseWorks(houseWorkId: Int): Flow<HouseWork> =
        remoteDataSource.getDetailHouseWorks(houseWorkId)

    suspend fun getDateHouseWorkList(
        fromDate: String,
        toDate: String
    ): Flow<Map<String, HouseWorks>> = remoteDataSource.getDateHouseWorkList(fromDate, toDate)

    suspend fun getPeriodHouseWorkListOfMember(
        teamMemberId: Int,
        fromDate: String,
        toDate: String
    ): Flow<Map<String, HouseWorks>> = remoteDataSource.getPeriodHouseWorkListOfMember(teamMemberId, fromDate, toDate)

    suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork> =
        remoteDataSource.getCompletedHouseWorkNumber(scheduledDate)

    suspend fun getList(scheduledDate: String): Flow<List<HouseWorks>> =
        remoteDataSource.getList(scheduledDate)

    /**
     * presets
     */
    suspend fun getHouseWorkList(): Flow<List<ChoreList>> =
        remoteDataSource.getHouseWorkList()

    /**
     * rules
     */
    suspend fun createRule(rule: Rule): Flow<RuleResponses> =
        remoteDataSource.createRule(rule)
    suspend fun getRules(): Flow<RuleResponses> =
        remoteDataSource.getRules()
    suspend fun deleteRule(ruleId: Int): Flow<Response> =
        remoteDataSource.deleteRule(ruleId)

}