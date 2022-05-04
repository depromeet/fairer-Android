package com.depromeet.housekeeper.network.remote.model

import com.depromeet.housekeeper.model.Assignee
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class HouseWorksCreateResponse (
    val assignees: List<Assignee>,
    val houseWorkId: Int,
    val houseWorkName: String,
    val scheduledDate: String,
    val scheduledTime: String,
    val space: String,
    val success: Boolean,
    val successDateTime: String?
)