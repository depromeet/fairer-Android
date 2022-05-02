package com.depromeet.housekeeper.model


data class Chore(
    var assignees: List<Int> = listOf(0),
    var houseWorkName: String = "",
    var scheduleDate: String = "",
    var scheduleTime: String = "하루 종일",
    val space: String = ""
) {
    companion object {
       val EMPTY = Chore (
           assignees = listOf(0),
           houseWorkName = "",
           scheduleDate = "",
           scheduleTime = "하루 종일",
           space = ""
       )

    }
}
