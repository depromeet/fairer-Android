package com.depromeet.housekeeper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedbackHouseworkResponse(
    val feedbackNum: Int,
    val feedbackId: Int,
    val myFeedback: Boolean
) : Parcelable
