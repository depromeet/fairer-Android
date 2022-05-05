package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class AddHouseWorkResponse(
  val houseWorks: List<HouseWork>
)