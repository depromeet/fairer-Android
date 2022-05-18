package com.depromeet.housekeeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.ChoreList
import com.depromeet.housekeeper.model.DayOfWeek
import com.depromeet.housekeeper.network.remote.repository.Repository
import com.depromeet.housekeeper.util.dayMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTodoFragment1ViewModel : ViewModel() {
  init {
    // getChoreList()
  }
  private val _chorepreset: MutableStateFlow<List<ChoreList>> = MutableStateFlow(listOf())
  val chorepreset: StateFlow<List<ChoreList>>
    get() = _chorepreset

  private val _chorelist: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
  val chorelist: StateFlow<List<String>>
    get() = _chorelist

  private val _selectSpace: MutableStateFlow<String> = MutableStateFlow("")
  val selectSpace: StateFlow<String>
    get() = _selectSpace

  private val _chores: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
  val chores: StateFlow<List<String>>
    get() = _chores

  fun setChoreList(space: String) {
    for (i in 0 until _chorepreset.value.size){
      if(space == _chorepreset.value[i].space){
        _chorelist.value = _chorepreset.value[i].houseWorks
      }
    }
  }


  fun setSpace(space: String) {
    _selectSpace.value = space
  }

  fun getChoreCount(): Int {
    return _chores.value.size
  }

  fun updateChores(chores: String, isSelect: Boolean) {
    when {
      isSelect -> if (!_chores.value.contains(chores)) {
        _chores.value = _chores.value.plus(chores)
      }
      else -> if (_chores.value.contains(chores)) {
        _chores.value = _chores.value.minus(chores)
      }
    }
  }

  private fun getChoreList() {
    viewModelScope.launch {
      Repository.getHouseWorkList().collect {
        _chorepreset.value = it.preset
      }
    }
  }

  private val calendar: Calendar = Calendar.getInstance().apply {
    set(Calendar.MONTH, this.get(Calendar.MONTH))
    firstDayOfWeek = Calendar.MONDAY
    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
  }

  private val _selectCalendar: MutableStateFlow<DayOfWeek> = MutableStateFlow(DayOfWeek(date = ""))
  val selectCalendar: StateFlow<DayOfWeek>
    get() = _selectCalendar

  fun updateSelectDate(date: String) {
    _selectCalendar.value = DayOfWeek(date)
  }

  fun updateCalendarView(year: Int, month: Int, dayOfMonth: Int) {
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

    val datePattern = "yyyy-MM-dd-EEE"
    _selectCalendar.value = DayOfWeek(SimpleDateFormat(datePattern, Locale.getDefault()).format(calendar.time))
  }

  fun addCalendarView(selectDate : String) {
    _selectCalendar.value = DayOfWeek(date = selectDate)
  }

  fun bindingDate(): String {
    // yyyy-mm-dd-eee
    val str = _selectCalendar.value.date.split("-")
    val day = dayMapper(str[3])
    return "${str[0]}년 ${str[1]}월 ${str[2]}일 $day"
  }
}