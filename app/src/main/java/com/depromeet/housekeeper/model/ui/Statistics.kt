package com.depromeet.housekeeper.model.ui

import com.depromeet.housekeeper.model.response.HouseWorkStatsMember
import com.depromeet.housekeeper.model.response.Member

data class Stats(
    val houseWorkName: String,
    val totalCount: Int,
    val members: List<HouseWorkStatsMember>
)

data class Ranker(
    val rank: Int = 0,
    val member: Member,
    val houseWorkCnt: Int,
)