package com.depromeet.housekeeper.model

import android.os.Parcelable
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