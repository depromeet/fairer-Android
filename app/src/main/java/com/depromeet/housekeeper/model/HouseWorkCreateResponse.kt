package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HouseWorkCreateResponse(
    val houseWorks: List<HouseWork>
) : Parcelable
