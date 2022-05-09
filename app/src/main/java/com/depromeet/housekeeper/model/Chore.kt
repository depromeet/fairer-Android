package com.depromeet.housekeeper.model

import androidx.annotation.Nullable
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
data class Chore(
    var assignees: List<Int> = listOf(5, 6),
    var houseWorkName: String = "",
    var scheduledDate: String = "yyyy-MM-dd",
    var scheduledTime: String? = null,
    var space: String = ""
) {
    companion object {
        const val DEFAULT_TIME = "하루 종일"
        const val ETC_SPACE = "ETC"
    }
}
