package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class DayOfWeek(
  val date: String,
  var isSelect: Boolean = false,
)