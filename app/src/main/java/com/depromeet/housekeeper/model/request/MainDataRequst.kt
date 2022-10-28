package com.depromeet.housekeeper.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

//todo repeat~
@Serializable
data class Chore(
    var assignees: List<Int> = listOf(),
    var houseWorkName: String = "",
    var repeatCycle: String? = RepeatCycle.ONCE.value,
    var repeatPattern: String = "yyyy-MM-dd", // ONCE -> "yyyy-MM-dd", DAILY -> "", WEEKLY -> "monday" , MONTHLY -> "5"
    var scheduledDate: String = "yyyy-MM-dd",
    var scheduledTime: String? = null, // ex) "10:00"
    var space: String = ""
) {
    companion object {
        const val DEFAULT_TIME = "하루 종일"
        const val ETC_SPACE = "ETC"
    }
}

enum class RepeatCycle(val value: String){
    ONCE("O"),
    DAYILY("D"),
    WEEKLY("W"),
    MONTHLY("M"),
}

enum class WeekDays(val eng: String, val kor: String) {
    MON("monday", "월"),
    TUE("tuesday", "화"),
    WED("wednesday", "수"),
    THR("thursday", "목"),
    FRI("friday", "금"),
    SAT("saturday","토"),
    SUN("sunday","일"),
    NONE("none","")
}

@Serializable
data class ChoreList(
    val space: String,
    val houseWorks: List<String>
)

@Serializable
data class Chores(
    val houseWorks: List<Chore>
)

@Parcelize
data class Rule(
    val ruleName: String,
) : Parcelable

@Parcelize
data class UpdateChoreBody(
    val toBeStatus: Int
) : Parcelable

@Parcelize
data class UpdateMember(
    val memberName: String,
    val profilePath: String
): Parcelable

@Parcelize
data class EditProfileModel(
    val memberName: String,
    val profilePath: String,
    val statusMessage: String
): Parcelable

