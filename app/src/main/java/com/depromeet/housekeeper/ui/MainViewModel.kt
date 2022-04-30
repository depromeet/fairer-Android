package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import com.depromeet.housekeeper.model.DayOfWeek
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainViewModel : ViewModel() {

  private val calendar: Calendar = Calendar.getInstance().apply {
    set(Calendar.MONTH, this.get(Calendar.MONTH))
    firstDayOfWeek = Calendar.MONDAY
    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
  }
  private val _selectDate: MutableStateFlow<String> =
    MutableStateFlow("${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월")
  val selectDate: StateFlow<String>
    get() = _selectDate

  fun getCurrentWeek(): List<DayOfWeek> {
    val format = SimpleDateFormat("YYYY-MM-dd-EEE", Locale.getDefault())
    val days = mutableListOf<String>()
    days.add(format.format(calendar.time))
    repeat(6) {
      calendar.add(Calendar.DATE, 1)
      days.add(format.format(calendar.time))
    }
      return days.map { DayOfWeek(date = it,isSelect = it == format.format(Calendar.getInstance().time)) }
  }

  fun getNextWeek(): MutableList<DayOfWeek> {
    return getWeek()
  }

  fun getLastWeek(): MutableList<DayOfWeek> {
    calendar.add(Calendar.DATE, -14)
    return getWeek()
  }

  fun updateSelectDate(date : String){
    _selectDate.value = "${calendar.get(Calendar.YEAR)}년 ${date}월"
  }

  private fun getWeek(): MutableList<DayOfWeek> {
    val format = SimpleDateFormat("YYYY-MM-dd-EEE", Locale.getDefault())
    val days = mutableListOf<String>()
    repeat(7) {
      calendar.add(Calendar.DATE, 1)
      days.add(format.format(calendar.time))
    }
    return days.map { DayOfWeek(date = it) }.toMutableList()
  }

  private val _completeChoreNum: MutableStateFlow<Int> =
    MutableStateFlow(17)
  val completeChoreNum: StateFlow<Int>
    get() = _completeChoreNum

  private val _remainChore: MutableStateFlow<Int> = MutableStateFlow(0)
  val remainChore: Int
    get() = _remainChore.value

  private val _endChore: MutableStateFlow<Int> = MutableStateFlow(0)
  val endChore: Int
    get() = _endChore.value

}