package com.depromeet.housekeeper.model.request

data class EditProfileModel(
    val memberName: String,
    val profilePath: String,
    val statusMessage: String
)