package com.depromeet.housekeeper.data.repository

import com.depromeet.housekeeper.data.dataSource.RemoteDataSourceImpl
import com.depromeet.housekeeper.model.response.ApiResult
import com.depromeet.housekeeper.model.response.HouseWorkStatsResponse
import com.depromeet.housekeeper.model.response.StatsListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class StatisticsRepository constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val externalScope: CoroutineScope? = null
) {
    suspend fun getStatistics(yearMonth: String): Flow<ApiResult<StatsListResponse>> =
        remoteDataSource.getStatsList(yearMonth)

    suspend fun getStatisticsRanking(yearMonth: String): Flow<ApiResult<HouseWorkStatsResponse>> =
        remoteDataSource.getStatsRanking(yearMonth)

    suspend fun getHoseWorkStatistics(
        houseWorkName: String,
        month: String
    ): Flow<ApiResult<HouseWorkStatsResponse>> =
        remoteDataSource.getHouseWorkStats(houseWorkName, month)
}