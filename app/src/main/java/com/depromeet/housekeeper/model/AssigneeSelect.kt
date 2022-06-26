package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssigneeSelect(
  val memberId: Int,
  val memberName: String,
  val profilePath: String,
  var selected: Boolean = false,
) : Parcelable

