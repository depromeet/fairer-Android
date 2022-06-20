package com.depromeet.housekeeper.network.remote.model

import kotlinx.serialization.Serializable


@Serializable
data class LoginResponse(
    val accessToken: String,
    val accessTokenExpireTime: String,
    val hasTeam : Boolean,
    val isNewMember: Boolean,
    val MemberName: String?,
    val refreshToken: String,
    val refreshTokenExpireTime: String
)
