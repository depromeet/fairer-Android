package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedbackCount(
    val comment: Int,
    val emoji_1: Int,
    val emoji_2: Int,
    val emoji_3: Int,
    val emoji_4: Int,
    val emoji_5: Int,
    val emoji_6: Int,
) : Parcelable
