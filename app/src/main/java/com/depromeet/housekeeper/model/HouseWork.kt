package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class HouseWork(
    val assignees: List<Assignee>,
    val houseWorkCompleteId: Int,
    val houseWorkId: Int,
    val houseWorkName: String,
    val repeatCycle: String,
    val repeatEndDate: String,
    val repeatPattern: String,
    val scheduledDate: String,
    val scheduledTime: String?,
    val space: String,
    val success: Boolean,
    val successDateTime: String?,
) : Parcelable