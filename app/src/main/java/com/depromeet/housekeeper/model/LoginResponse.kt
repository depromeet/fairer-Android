package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LoginResponse(
    val accessToken: String,
    val accessTokenExpireTime: String,
    val hasTeam: Boolean,
    val isNewMember: Boolean,
    val memberName: String?,
    val memberId: Int,
    val refreshToken: String,
    val refreshTokenExpireTime: String
) : Parcelable
