package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class ChoreList(
    val space : String,
    val houseWorks : List<String>

)
