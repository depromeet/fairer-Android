package com.depromeet.housekeeper.model.ui

import com.depromeet.housekeeper.model.response.HouseWorkStatsMember

data class Stats(
    val houseWorkName: String,
    val totalCount: Int,
    val members: List<HouseWorkStatsMember>
)