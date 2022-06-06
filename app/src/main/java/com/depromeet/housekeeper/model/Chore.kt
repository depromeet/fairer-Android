package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class Chore(
    var assignees: List<Int> = listOf(0),
    var houseWorkName: String = "",
    var scheduledDate: String = "yyyy-MM-dd",
    var scheduledTime: String? = null,
    var space: String = ""
) {
    companion object {
        const val DEFAULT_TIME = "하루 종일"
        const val ETC_SPACE = "ETC"
    }
}
