package com.depromeet.housekeeper.util

import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {
    val current: Calendar = Calendar.getInstance()
    val yearMonthFormat = SimpleDateFormat("yyyy년 MM월")

    fun getCalendarString(calendar: Calendar): String {
        return yearMonthFormat.format(calendar.time)
    }
}