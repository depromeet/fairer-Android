package com.depromeet.housekeeper.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class BuildTeamResponse(
    val inviteCode: String,
    val teamId: Int
) : Parcelable

