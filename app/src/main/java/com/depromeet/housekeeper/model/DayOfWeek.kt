package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DayOfWeek(
  val date: String, // ex) 2022-08-13-토
  var isSelect: Boolean = false
) : Parcelable