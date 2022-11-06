package com.depromeet.housekeeper.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class BuildTeamResponse(
    val inviteCode: String,
    val teamId: Int
) : Parcelable

@Parcelize
data class ErrorResponse(
    val code: Int,
    val errorMessage: String,
    val referrerUrl: String
) : Parcelable

@Parcelize
data class GetInviteCode(
    val inviteCode: String,
    val inviteCodeExpirationDateTime: String,
    val teamName: String
) : Parcelable

@Parcelize
data class InviteFailedResponse(
    val code: Int,
    val errorMessage: String,
    val referrerUrl: String
) : Parcelable

@Parcelize
data class JoinTeamResponse(
    val memberNames: List<String>,
    val teamId: Int
) : Parcelable

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

@Parcelize
data class ProfileData(
    val memberName: String,
    val profilePath: String,
    val statusMessage: String = "",
) : Parcelable

@Parcelize
data class ProfileImages(
    val bigImageList: List<String>,
) : Parcelable

@Parcelize
data class TeamUpdateResponse(
    val teamId: Int,
    val teamName: String
) : Parcelable

@Parcelize
data class UpdateMemberResponse(
    val code: Int,
    val message: String
) : Parcelable

