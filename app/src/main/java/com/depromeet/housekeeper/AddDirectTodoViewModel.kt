package com.depromeet.housekeeper

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddDirectTodoViewModel : ViewModel() {
    private val _curDate: MutableStateFlow<String> =
        MutableStateFlow("2022년 * 4월 23일 토요일")
    val curDate: StateFlow<String>
        get() = _curDate

    private val _curSpace: MutableStateFlow<String> =
        MutableStateFlow("기타 공간")
    val curSpace: StateFlow<String>
        get() = _curSpace

    private val calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    private val _selectCalendar: MutableStateFlow<String> = MutableStateFlow("")
    val selectCalendar: StateFlow<String>
        get() = _selectCalendar

    fun updateCalendarView(year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val datePattern = "yyyy-MM-dd-EEE"
        _selectCalendar.value = SimpleDateFormat(datePattern, Locale.getDefault()).format(calendar.time)
    }

}