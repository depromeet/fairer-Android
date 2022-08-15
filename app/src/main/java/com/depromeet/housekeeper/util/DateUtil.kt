package com.depromeet.housekeeper.util

import android.util.Log
import com.depromeet.housekeeper.model.DayOfWeek
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    private const val TAG = "DateUtil"

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    fun getFromDateToDateOfWeek(date: String): List<String> {
        val dateArray = date.split("-").toTypedArray()
        val year = dateArray[0].toInt()
        val mm = dateArray[1].toInt()
        val dd = dateArray[2].toInt()
        val cal = Calendar.getInstance()

        cal[year, mm - 1] = dd
        cal.firstDayOfWeek = Calendar.SUNDAY
        val dayOfWeek = cal[Calendar.DAY_OF_WEEK]
        
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek+1)
        val fromDate = dateFormat.format(cal.time)
        
        cal.add(Calendar.DAY_OF_MONTH, 6)
        val toDate = dateFormat.format(cal.time)

        Log.d(TAG, "getFromDateToDateOfWeek: ${fromDate}, ${toDate}")
        return listOf(fromDate, toDate)
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
        Timber.d("MAIN : getCurrentWeek : ${days}")
        return days.map {
            DayOfWeek(
                date = it,
                isSelect = it == format.format(Calendar.getInstance().time)
            )
        }.toMutableList()
    }

}