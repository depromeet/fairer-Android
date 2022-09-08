package com.depromeet.housekeeper.model.response

data class RuleResponses(
    val ruleResponseDtos: List<RuleResponse>,
    val teamId: Int
)