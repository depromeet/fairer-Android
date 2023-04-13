package com.depromeet.housekeeper.model.response

import android.os.Parcelable
import com.depromeet.housekeeper.model.Assignee
import kotlinx.parcelize.Parcelize

data class CompleteHouseWork(
    val count: Int
)

data class EditResponseBody(
    val code: Int,
    val message: String
)

data class Groups(
    val members: List<Assignee>,
    val teamId: Int,
    val teamName: String
)

@Parcelize
data class HouseWork(
    val assignees: List<Assignee>,
    val houseWorkCompleteId: Int? = 0,
    val houseWorkId: Int,
    val houseWorkName: String,
    val repeatCycle: String? ="",
    val repeatEndDate: String? = "",
    val repeatPattern: String? = "",
    val scheduledDate: String,
    val scheduledTime: String?,
    val space: String,
    val success: Boolean,
    val successDateTime: String?,
) : Parcelable

@Parcelize
data class HouseWorks(
    val countDone: Int,
    val countLeft: Int,
    val houseWorks: List<HouseWork>,
    val scheduledDate: String,
    val memberId: Int,
) : Parcelable

data class Response(
    val code: Int,
    val message: String
)

data class RuleResponse(
    val ruleId: Int,
    val ruleName: String
)

data class RuleResponses(
    val ruleResponseDtos: List<RuleResponse>,
    val teamId: Int
)

data class UpdateChoreResponse(
    val houseWorkCompleteId: Int
)

data class FeedbackListModel (
    val feedbackCount: Long,
    val feedbackFindOneResponseDtoList: List<FeedbackFindOneResponseDtoList>
)

data class FeedbackFindOneResponseDtoList (
    val comment: String,
    val emoji: Long,
    val memberName: String,
    val profilePath: String
)
