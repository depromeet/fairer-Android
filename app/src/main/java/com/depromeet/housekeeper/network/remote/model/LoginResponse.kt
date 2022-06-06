package com.depromeet.housekeeper.network.remote.model

import kotlinx.serialization.Serializable


@Serializable
data class LoginResponse(
    val accessToken: String,
    val accessTokenExpireTime: String,
    val isNewMember: Boolean,
    val refreshToken: String,
    val refreshTokenExpireTime: String
)
