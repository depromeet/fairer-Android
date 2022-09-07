package com.depromeet.housekeeper.util

import com.depromeet.housekeeper.model.enums.DayString

fun parseDate(date: String): Triple<String, String, String> {
    val str = date.split("-")
    return Triple(str[0], str[1], str[2])
}

fun dayMapper(day: String): String {
    return when (day) {
        "Mon" -> DayString.Mon.korDay
        "월" -> DayString.Mon.korDay
        "Tue" -> DayString.Tue.korDay
        "화" -> DayString.Tue.korDay
        "Wed" -> DayString.Wed.korDay
        "수" -> DayString.Wed.korDay
        "Thu" -> DayString.Thu.korDay
        "목" -> DayString.Thu.korDay
        "Fri" -> DayString.Fri.korDay
        "금" -> DayString.Fri.korDay
        "Sat" -> DayString.Sat.korDay
        "토" -> DayString.Sat.korDay
        "Sun" -> DayString.Sun.korDay
        "일" -> DayString.Sun.korDay
        else -> DayString.ETC.korDay
    }
}
