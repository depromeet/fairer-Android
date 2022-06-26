package com.depromeet.housekeeper.model

data class ErrorResponse(
    val code: Int,
    val errorMessage: String,
    val referrerUrl: String
)