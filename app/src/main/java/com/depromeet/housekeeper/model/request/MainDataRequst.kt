package com.depromeet.housekeeper.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

//todo repeat~
@Serializable
data class Chore(
    var assignees: List<Int> = listOf(),
    var houseWorkName: String = "",
    var repeatCycle: String? = "0",
    var repeatEndDate: String? = "yyyy-MM-dd",
    var scheduledDate: String = "yyyy-MM-dd",
    var scheduledTime: String? = null,
    var space: String = ""
) {
    companion object {
        const val DEFAULT_TIME = "하루 종일"
        const val ETC_SPACE = "ETC"
    }
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

