package com.depromeet.housekeeper.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class InviteFailedResponse(
    val code: Int,
    val errorMessage: String,
    val referrerUrl: String
) : Parcelable
