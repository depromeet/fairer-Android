package com.depromeet.housekeeper.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ChoreList(
    val space: String,
    val houseWorks: List<String>

)
