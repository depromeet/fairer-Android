package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
    Main Profile Adapter 를 위한 임시 data class
 */
@Parcelize
data class AssigneeTemp(
    val memberId: Int,
    val memberName: String,
): Parcelable

