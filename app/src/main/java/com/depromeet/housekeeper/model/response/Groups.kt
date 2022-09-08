package com.depromeet.housekeeper.model.response

import com.depromeet.housekeeper.model.Assignee

data class Groups(
    val members: List<Assignee>,
    val teamId: Int,
    val teamName: String
)