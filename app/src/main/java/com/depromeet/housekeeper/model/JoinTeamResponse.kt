package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class JoinTeamResponse(
    val memberNames : List<String>,
    val teamId : Int
):Parcelable
