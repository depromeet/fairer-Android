package com.depromeet.housekeeper.model.response

import android.os.Parcelable
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.HouseWork
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
data class HouseWorkCreateResponse(
    val houseWorks: List<HouseWork>
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
    val houseWorkId: Int,
    val success: Boolean
)
