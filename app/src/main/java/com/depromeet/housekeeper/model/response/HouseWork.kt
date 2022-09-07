package com.depromeet.housekeeper.model.response

import android.os.Parcelable
import com.depromeet.housekeeper.model.Assignee
import kotlinx.parcelize.Parcelize

@Parcelize
data class HouseWork(
    val assignees: List<Assignee>,
    val houseWorkId: Int,
    val houseWorkName: String,
    val scheduledDate: String,
    val scheduledTime: String?,
    val space: String,
    val success: Boolean,
    val successDateTime: String?,
    var now: Int = 0,
) : Parcelable