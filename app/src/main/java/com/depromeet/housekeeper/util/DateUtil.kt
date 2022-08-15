package com.depromeet.housekeeper.util

import com.depromeet.housekeeper.model.DayOfWeek
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtil {

    val datePattern = "yyyy-MM-dd"
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val fullDateFormat = SimpleDateFormat("yyyy-MM-dd-EEE", Locale.getDefault())


    fun getCurrentStartDate(): String {
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, this.get(Calendar.MONTH))
            firstDayOfWeek = Calendar.SUNDAY
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }
        return fullDateFormat.format(calendar.time)
    }

    fun getStartDate(date: String): String {
        val dateArray = date.split("-").toTypedArray()
        val year = dateArray[0].toInt()
        val mm = dateArray[1].toInt()
        val dd = dateArray[2].toInt()
        val cal = Calendar.getInstance()

        cal[year, mm - 1] = dd
        cal.firstDayOfWeek = Calendar.SUNDAY

        val dayOfWeek = cal[Calendar.DAY_OF_WEEK]
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek+1)
        return dateFormat.format(cal.time)
    }

    fun getLastDate(startDate: String): String {
        var localDate = LocalDate.parse(startDate)
        localDate = localDate.plusDays(6)
        return localDate.format(DateTimeFormatter.ofPattern(datePattern))
    }

    fun getCurrentWeek(): MutableList<DayOfWeek> {
        val format = SimpleDateFormat("yyyy-MM-dd-EEE", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, this.get(Calendar.MONTH))
            firstDayOfWeek = Calendar.SUNDAY
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }
        val days = mutableListOf<String>()
        days.add(format.format(calendar.time))
        Timber.d("$DATE_UTIL_TAG: getCurrentWeek : ${days}")

        repeat(6) {
            calendar.add(Calendar.DATE, 1)
            days.add(format.format(calendar.time))
        }
        Timber.d("$DATE_UTIL_TAG: getCurrentWeek : ${days}")
        return days.map {
            DayOfWeek(
                date = it,
                isSelect = it == format.format(Calendar.getInstance().time)
            )
        }.toMutableList()
    }

    fun getTodayFull(): DayOfWeek {
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.MONTH, this.get(Calendar.MONTH))
            firstDayOfWeek = Calendar.SUNDAY
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }
        getCurrentWeek()
        return DayOfWeek(
            date = fullDateFormat.format(Date(System.currentTimeMillis())),
            isSelect = true
        )
    }

}