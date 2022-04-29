package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainViewModel : ViewModel() {
  private val calendar = Calendar.getInstance()

  private val _currentDate: MutableStateFlow<String> =
    MutableStateFlow("${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월")
  val currentDate: String
    get() = _currentDate.value

  private val _completeChoreNum: MutableStateFlow<Int> =
    MutableStateFlow(17)
  val completeChoreNum: StateFlow<Int>
    get() = _completeChoreNum

  fun setCalendar(): Int {
    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    return calendar.get(Calendar.DAY_OF_MONTH)
  }

  private val currentMonday = MutableStateFlow(setCalendar())

  fun getCurrentWeek(): List<String> {
    val format = SimpleDateFormat("MM-dd", Locale.getDefault())
    val days = mutableListOf<String>()
    days.add(format.format(calendar.time))
    repeat(6) {
      calendar.add(Calendar.DATE, 1)
      days.add(format.format(calendar.time))
    }
    return days
  }

  fun getNextWeek(): List<String> {
    val format = SimpleDateFormat("MM-dd", Locale.getDefault())
    val days = mutableListOf<String>()
    repeat(7) {
      calendar.add(Calendar.DATE, 1)
      days.add(format.format(calendar.time))
    }
    return days
  }

  fun getLastWeek(): List<String> {
    calendar.add(Calendar.DATE,-14)
    val format = SimpleDateFormat("M-dd", Locale.getDefault())
    val days = mutableListOf<String>()
    repeat(7) {
      calendar.add(Calendar.DATE, 1)
      days.add(format.format(calendar.time))
    }
    return days
  }

  private val _remainChore: MutableStateFlow<Int> = MutableStateFlow(0)
  val remainChore: Int
    get() = _remainChore.value

  private val _endChore: MutableStateFlow<Int> = MutableStateFlow(0)
  val endChore: Int
    get() = _endChore.value

}