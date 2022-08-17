package com.depromeet.housekeeper.model

data class Groups(
    val members: List<Assignee>,
    val teamId: Int,
    val teamName: String
)