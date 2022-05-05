package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class AddHouseWork(
    val assignees: List<Int>,
    val houseWorkName: String,
    val scheduledDate: String,
    val scheduledTime: String,
    val space: String
)