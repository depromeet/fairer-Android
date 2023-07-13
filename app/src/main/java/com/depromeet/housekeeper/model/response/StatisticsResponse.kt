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
): Comparable<HouseWorkStatsMember> {
    override fun compareTo(other: HouseWorkStatsMember): Int =
    when {
        houseWorkCount < other.houseWorkCount -> 1
        houseWorkCount > other.houseWorkCount -> -1
        else -> 0
    }
}

data class Member(
    val memberId: Int,
    val memberName: String,
    val profilePath: String = "",
) : Comparable<Member> {
    override fun compareTo(other: Member) = when {
        memberName < other.memberName -> 1
        memberName > other.memberName -> -1
        else -> 0
    }


}