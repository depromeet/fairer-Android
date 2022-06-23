package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class JoinTeamResponse(
    val memberName : List<String>,
    val teamId : Int
):Parcelable
