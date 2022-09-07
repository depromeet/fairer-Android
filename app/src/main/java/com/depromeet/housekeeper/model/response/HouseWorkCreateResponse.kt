package com.depromeet.housekeeper.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HouseWorkCreateResponse(
    val houseWorks: List<HouseWork>
) : Parcelable
