package com.depromeet.housekeeper.model.response

import android.os.Parcelable
import com.depromeet.housekeeper.model.HouseWork
import kotlinx.parcelize.Parcelize

@Parcelize
data class HouseWorks(
    val countDone: Int,
    val countLeft: Int,
    val houseWorks: List<HouseWork>,
    val scheduledDate: String,
    val memberId: Int,
) : Parcelable