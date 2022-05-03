package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class HouseWork(
  val assignees: List<Assignee>?,
  val houseWorkId: Int,
  val houseWorkName: String,
  val scheduledDate: String,
  val scheduledTime: String,
  val space: String,
  val success: Boolean,
  val successDateTime: String?,
)