package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class HouseWorks(
    val countDone: Int,
    val countLeft: Int,
    val houseWorks: List<HouseWork>,
    val scheduledDate: String
)