package com.depromeet.housekeeper.model.request

import android.os.Parcelable
import com.depromeet.housekeeper.model.Assignee
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Chore(
    var assignees: List<Int> = listOf(),
    var houseWorkName: String = "",
    var repeatCycle: String? = RepeatCycle.ONCE.value,
    var repeatPattern: String = "yyyy-MM-dd", // ONCE -> "yyyy-MM-dd", DAILY -> "", WEEKLY -> "monday" , MONTHLY -> "5"
    var scheduledDate: String = "yyyy-MM-dd",
    var scheduledTime: String? = null, // ex) "10:00"
    var space: String = ""
): Parcelable {
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
    MON("MONDAY", "월"),
    TUE("TUESDAY", "화"),
    WED("WEDNESDAY", "수"),
    THR("THURSDAY", "목"),
    FRI("FRIDAY", "금"),
    SAT("SATURDAY","토"),
    SUN("SUNDAY","일"),
    NONE("NONE","")
}

@Parcelize
data class EditChore(
    var assignees: List<Int> = listOf(),
    var houseWorkId: Int,
    var houseWorkName: String = "",
    var repeatCycle: String? = RepeatCycle.ONCE.value,
    var repeatEndDate: String? = "",
    var repeatPattern: String? = "yyyy-MM-dd", // ONCE -> "yyyy-MM-dd", DAILY -> "", WEEKLY -> "monday" , MONTHLY -> "5"
    var scheduledDate: String = "yyyy-MM-dd",
    var scheduledTime: String? = null, // ex) "10:00"
    var space: String = "",
    var type: String = EditType.ONLY.value,
    var updateStandardDate: String,
): Parcelable

@Parcelize
data class DeleteChoreRequest(
    val deleteStandardDate: String,
    val houseWorkId: Int,
    val type: String = EditType.NONE.value
): Parcelable

enum class EditType(val value: String){
    ONLY("O"),
    FORWARD("H"),
    ALL("A"),
    NONE("N")
}


@Serializable
data class ChoreList(
    val space: String,
    val houseWorks: List<String>
)

@Parcelize
data class Chores(
    val houseWorks: List<Chore>
): Parcelable

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

@Parcelize
data class CreateFeedbackModel(
    val comment: String,
    val emoji : Int,
    val houseCompleteId: Int
):Parcelable

