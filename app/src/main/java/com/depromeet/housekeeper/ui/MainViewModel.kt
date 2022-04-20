package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

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

  fun getMondayNumDay(): Int {
    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
    calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH))
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    return calendar.get(Calendar.DAY_OF_MONTH)
  }

  private val _remainChore: MutableStateFlow<Int> = MutableStateFlow(0)
  val remainChore: Int
    get() = _remainChore.value

  private val _endChore: MutableStateFlow<Int> = MutableStateFlow(0)
  val endChore: Int
    get() = _endChore.value

}