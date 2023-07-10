package com.depromeet.housekeeper.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SocialType(
    val socialType: String
): Parcelable

@Parcelize
data class BuildTeam(
    val teamName: String
): Parcelable

@Parcelize
data class JoinTeam(
    val inviteCode: String
): Parcelable

@Parcelize
data class Message(
    val body: String,
    val memberId: Int,
    val title: String,
): Parcelable

@Parcelize
data class Token(
    val token: String
): Parcelable

@Parcelize
data class Alarm(
    val scheduledTimeStatus: Boolean,
    val notCompleteStatus : Boolean
): Parcelable
