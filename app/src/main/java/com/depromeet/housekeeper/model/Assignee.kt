package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class Assignee(
    val memberId: Int,
    val memberName: String,
    val profilePath: String
)