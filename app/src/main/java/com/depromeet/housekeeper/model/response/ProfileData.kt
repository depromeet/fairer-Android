package com.depromeet.housekeeper.model.response

data class ProfileData(
    val memberName: String,
    val profilePath: String,
    val statusMessage: String = "",
)