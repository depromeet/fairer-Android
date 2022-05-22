package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Assignee(
    val memberId: Int,
    val memberName: String,
): Parcelable
