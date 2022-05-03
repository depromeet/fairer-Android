package com.depromeet.housekeeper.model

data class HouseWorks(
    val countDone: Int,
    val countLeft: Int,
    val houseWorks: List<HouseWork>,
    val scheduledDate: String
)