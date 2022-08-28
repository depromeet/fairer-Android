package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetInviteCode(
    val inviteCode: String,
    val inviteCodeExpirationDateTime: String,
    val teamName: String
) : Parcelable
