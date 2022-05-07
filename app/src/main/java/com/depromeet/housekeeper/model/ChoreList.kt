package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class ChoreList(
    val houseWorks : List<String>,
    val space : String
)
