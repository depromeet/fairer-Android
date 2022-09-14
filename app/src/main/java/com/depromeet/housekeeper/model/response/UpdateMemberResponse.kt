package com.depromeet.housekeeper.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateMemberResponse(
    val code: Int,
    val message: String
) : Parcelable