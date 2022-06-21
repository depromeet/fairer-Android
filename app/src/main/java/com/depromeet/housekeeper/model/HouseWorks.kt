package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HouseWorks(
  val countDone: Int,
  val countLeft: Int,
  val houseWorks: List<HouseWork>,
  val scheduledDate: String,
  val memberId: Int,
) : Parcelable