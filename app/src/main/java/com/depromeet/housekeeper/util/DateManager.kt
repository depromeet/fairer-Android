package com.depromeet.housekeeper.util

import com.depromeet.housekeeper.model.enums.DayString

fun parseDate(date: String): Triple<String, String, String> {
    val str = date.split("-")
    return Triple(str[0], str[1], str[2])
}

fun dayMapper(day: String):String {
    return when(day) {
        "Mon" -> DayString.Mon.korDay
        "Tue" -> DayString.Tue.korDay
        "Wed" -> DayString.Wed.korDay
        "Thu" -> DayString.Thu.korDay
        "Fri" -> DayString.Fri.korDay
        "Sat" -> DayString.Sat.korDay
        "Sun" -> DayString.Sun.korDay
        else -> DayString.Mon.korDay
    }
}
