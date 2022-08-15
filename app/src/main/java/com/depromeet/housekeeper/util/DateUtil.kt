package com.depromeet.housekeeper.util

import com.depromeet.housekeeper.model.DayOfWeek
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val fullDateFormat = SimpleDateFormat("yyyy-MM-dd-EEE", Locale.getDefault())

    fun getFromDateToDateOfWeek(date: String): List<String> {
        val dateArray = date.split("-").toTypedArray()
        val year = dateArray[0].toInt()
        val mm = dateArray[1].toInt()
        val dd = dateArray[2].toInt()
        val cal = Calendar.getInstance()

        cal[year, mm - 1] = dd
        cal.firstDayOfWeek = Calendar.SUNDAY

        val fromDate = getFirstDate(cal)
        val toDate = getLastDate(cal)

        Timber.d("$DATE_UTIL_TAG: getFromDateToDateOfWeek: ${fromDate}, ${toDate}")
        return listOf(fromDate, toDate)
    }

    fun getFirstDate(cal: Calendar): String {
        val dayOfWeek = cal[Calendar.DAY_OF_WEEK]
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek + 1)
        return dateFormat.format(cal.time)
    }

    fun getLastDate(cal: Calendar): String {
        cal.add(Calendar.DAY_OF_MONTH, 6)
        return dateFormat.format(cal.time)
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

    fun getTodayDateOnly(): String {
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.MONTH, this.get(Calendar.MONTH))
            firstDayOfWeek = Calendar.SUNDAY
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }
        getCurrentWeek()
        return dateFormat.format(Date(System.currentTimeMillis()))
    }
}