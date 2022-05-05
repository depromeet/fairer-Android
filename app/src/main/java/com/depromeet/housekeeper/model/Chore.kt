package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class Chore(
    var assignees: List<Int> = listOf(5, 6),
    var houseWorkName: String = "",
    var scheduledDate: String = "yyyy-MM-dd",
    var scheduledTime: String = DEFAULT_TIME,
    var space: String = ""
) {
    companion object {
        const val DEFAULT_TIME = "하루 종일"
    }
}
