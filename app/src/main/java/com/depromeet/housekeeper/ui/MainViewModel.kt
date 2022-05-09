package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.DayOfWeek
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainViewModel : ViewModel() {

  init {
    getHouseWorks()
    getCompleteHouseWorkNumber()
  }

  private val calendar: Calendar = Calendar.getInstance().apply {
    set(Calendar.MONTH, this.get(Calendar.MONTH))
    firstDayOfWeek = Calendar.MONDAY
    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
  }

  fun getCalendar(): Calendar {
    return this.calendar
  }

  private val datePattern = "yyyy-MM-dd-EEE"
  private val format = SimpleDateFormat(datePattern, Locale.getDefault())

  private val _dayOfWeek: MutableStateFlow<DayOfWeek> =
    MutableStateFlow(DayOfWeek(date = format.format(Date(System.currentTimeMillis()))))
  val dayOfWeek: StateFlow<DayOfWeek>
    get() = _dayOfWeek

  fun getCurrentWeek(): MutableList<DayOfWeek> {
    val format = SimpleDateFormat(datePattern, Locale.getDefault())
    val days = mutableListOf<String>()
    days.add(format.format(calendar.time))
    repeat(6) {
      calendar.add(Calendar.DATE, 1)
      days.add(format.format(calendar.time))
    }
    return days.map {
      DayOfWeek(
        date = it,
        isSelect = it == format.format(Calendar.getInstance().time)
      )
    }.toMutableList()
  }

  fun updateSelectDate(date: DayOfWeek) {
    _dayOfWeek.value = date
  }

  fun getNextWeek(): MutableList<DayOfWeek> {
    return getWeek()
  }

  fun getLastWeek(): MutableList<DayOfWeek> {
    calendar.add(Calendar.DATE, -14)
    return getWeek()
  }

  private fun getWeek(): MutableList<DayOfWeek> {
    val format = SimpleDateFormat(datePattern, Locale.getDefault())
    val days = mutableListOf<String>()
    repeat(7) {
      calendar.add(Calendar.DATE, 1)
      days.add(format.format(calendar.time))
    }
    return days.map { DayOfWeek(date = it) }.toMutableList()
  }

  private val _completeChoreNum: MutableStateFlow<Int> =
    MutableStateFlow(0)
  val completeChoreNum: StateFlow<Int>
    get() = _completeChoreNum

  private val _houseWorks: MutableStateFlow<HouseWorks?> = MutableStateFlow(null)
  val houseWorks: StateFlow<HouseWorks?>
    get() = _houseWorks
  
  private val _currentState: MutableStateFlow<CurrentState?> = MutableStateFlow(CurrentState.REMAIN)
  val currentState: StateFlow<CurrentState?>
    get() = _currentState

  private fun getHouseWorks() {
    val requestFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    viewModelScope.launch {
      Repository.getList(requestFormat.format(Calendar.getInstance().time)).collect {
        _houseWorks.value = it
      }
    }
  }

  private fun getCompleteHouseWorkNumber() {
    val requestFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    viewModelScope.launch {
      Repository.getCompletedHouseWorkNumber(requestFormat.format(Calendar.getInstance().time))
        .collect {
          _completeChoreNum.value = it.count
        }
    }
  }
  fun updateState(afterState : CurrentState) {
    _currentState.value = afterState
  }

  enum class CurrentState {
    REMAIN,
    DONE
  }
}