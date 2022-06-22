package com.depromeet.housekeeper.model

data class GetInviteCode(
    val inviteCode: String,
    val inviteCodeExpirationDateTime: String,
    val teamName: String
)
