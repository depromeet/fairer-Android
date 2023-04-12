package com.depromeet.housekeeper.model.response

import com.squareup.moshi.Json

data class StatisticsListResponse(
    @Json(name = "statisticsResponse") val statisticsList: List<StatisticsStatus>
)
data class StatisticsStatus(
    val houseWorkCount: Int,
    val houseWorkName: String,
)

data class HouseWorkStatisticsResponse(
    @Json(name = "houseWorkStatics") val houseWorkStatisticsList: List<HouseWorkStatisticsStatus>
)
data class HouseWorkStatisticsStatus(
    val houseWorkCount: Int,
    val member: Member,
)
data class Member(
    val memberId: Int,
    val memberName: String,
    val profilePath: String = "",
)