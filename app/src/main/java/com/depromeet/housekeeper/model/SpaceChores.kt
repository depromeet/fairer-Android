package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpaceChores(
    var spaceName: String,
    var houseWorks: List<String>,
) : Parcelable