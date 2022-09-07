package com.depromeet.housekeeper.model.request

data class Message(
    val body: String,
    val memberId: Int,
    val title: String,
)
