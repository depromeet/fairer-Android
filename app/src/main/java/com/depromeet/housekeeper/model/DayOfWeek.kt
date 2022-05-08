package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DayOfWeek(
  val date: String,
  var isSelect: Boolean = false,
) : Parcelable