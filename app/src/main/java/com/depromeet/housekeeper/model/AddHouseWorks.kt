package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class AddHouseWorks(
  val houseWorks: List<AddHouseWork>,
)