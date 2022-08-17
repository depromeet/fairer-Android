package com.depromeet.housekeeper.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SampleResponse(
  val breeds: List<String>?,
  val height: Int,
  val id: String,
  val url: String,
  val width: Int,
)