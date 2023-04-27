package com.depromeet.housekeeper.model.response

import com.squareup.moshi.Json

data class StatsListResponse(
    @Json(name = "statisticResponseDtos") val statisticsList: List<StatsStatus>
)
data class StatsStatus(
    val houseWorkCount: Int,
    val houseWorkName: String,
)

data class HouseWorkStatsResponse(
    @Json(name = "houseWorkStatics") val houseWorkStatisticsList: List<HouseWorkStatsMember>
)
data class HouseWorkStatsMember(
    val houseWorkCount: Int,
    val member: Member,
)
data class Member(
    val memberId: Int,
    val memberName: String,
    val profilePath: String = "",
)
