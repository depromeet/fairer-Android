package com.depromeet.housekeeper.model.request

import kotlinx.serialization.Serializable

@Serializable
data class Chores(
    val houseWorks: List<Chore>,
)