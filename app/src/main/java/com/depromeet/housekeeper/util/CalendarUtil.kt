package com.depromeet.housekeeper.util

import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {
    private val yearMonthKorFormat = SimpleDateFormat("yyyy년 MM월")


    fun getCalendarKorString(calendar: Calendar): String {
        return yearMonthKorFormat.format(calendar.time)
    }


}