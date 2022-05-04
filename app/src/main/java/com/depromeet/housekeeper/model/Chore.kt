package com.depromeet.housekeeper.model


data class Chore(
    var assignees: List<Int> = listOf(0),
    var houseWorkName: String = "",
    var scheduleDate: String = "",
    var scheduleTime: String = DEFAULT_TIME,
    val space: String = ""
) {
    companion object {
        const val DEFAULT_TIME = "하루 종일"
    }
}
