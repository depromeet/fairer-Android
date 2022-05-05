package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class Chores(
    val houseWorks: List<Chore>,
)