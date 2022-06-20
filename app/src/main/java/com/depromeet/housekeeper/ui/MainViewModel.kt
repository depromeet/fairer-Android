package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.AssigneeTemp
import com.depromeet.housekeeper.model.DayOfWeek
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.UpdateChoreBody
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
    getCompleteHouseWorkNumber()
    getGroupName()
  }

  private val calendar: Calendar = Calendar.getInstance().apply {
    set(Calendar.MONTH, this.get(Calendar.MONTH))
    firstDayOfWeek = Calendar.MONDAY
    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
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

  fun getDatePickerWeek(year: Int, month: Int, dayOfMonth: Int): MutableList<DayOfWeek> {
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

    val format = SimpleDateFormat(datePattern, Locale.getDefault())
    val selectDate = format.format(calendar.time)
    _dayOfWeek.value = DayOfWeek(selectDate, true)

    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

    val days = mutableListOf<String>()
    days.add(format.format(calendar.time))
    repeat(6) {
      calendar.add(Calendar.DATE, 1)
      days.add(format.format(calendar.time))
    }
    return days.map {
      DayOfWeek(
        date = it,
        isSelect = it == selectDate
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

  private val _myHouseWorks: MutableStateFlow<HouseWorks?> = MutableStateFlow(null)
  val myHouseWorks: StateFlow<HouseWorks?>
    get() = _myHouseWorks

  private val _currentState: MutableStateFlow<CurrentState?> = MutableStateFlow(CurrentState.REMAIN)
  val currentState: StateFlow<CurrentState?>
    get() = _currentState

  private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val networkError: StateFlow<Boolean>
    get() = _networkError

  fun getHouseWorks() {
    //TODO 성능 개선 필요
    val dayOfWeekDate = dayOfWeek.value.date
    val lastIndex = dayOfWeekDate.indexOfLast { it == '-' }
    val requestDate = dayOfWeekDate.dropLast(dayOfWeekDate.length - lastIndex)
    viewModelScope.launch {
      Repository.getList(requestDate)
        .runCatching {
          collect {
            _myHouseWorks.value = it.first()
          }
        }.onFailure {
        //  _networkError.value = true
        }
    }
    getCompleteHouseWorkNumber()
  }

  private fun getCompleteHouseWorkNumber() {
    val requestFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    viewModelScope.launch {
      Repository.getCompletedHouseWorkNumber(requestFormat.format(Calendar.getInstance().time))
        .runCatching {
          collect {
            _completeChoreNum.value = it.count
          }
        }.onFailure {
          _networkError.value = true
        }
    }
  }

  fun updateState(afterState: CurrentState) {
    _currentState.value = afterState
  }

  fun updateChoreState(houWorkId: Int) {
    val toBeStatus = when (currentState.value) {
      CurrentState.REMAIN -> 1
      else -> 0
    }
    viewModelScope.launch {
      Repository.updateChoreState(
        houseWorkId = houWorkId,
        updateChoreBody = UpdateChoreBody(toBeStatus)
      ).runCatching {
        collect {
          getHouseWorks()
        }
      }.onFailure {
        _networkError.value = true
      }
    }
  }

  private val _groupName: MutableStateFlow<String> = MutableStateFlow("")
  val groupName: StateFlow<String>
    get() = _groupName

  private fun getGroupName() {
    viewModelScope.launch {
      Repository.getTeam().runCatching {
        collect {
          _groupName.value = it.teamName
        }
      }

    }
  }

  /*
    Group Adapter 를 위한 임시 변수
   */
  private val tempAssign = mutableListOf(
    AssigneeTemp(0, "고가혜"),
    AssigneeTemp(1, "권진혁"),
    AssigneeTemp(2, "최지혜"),
    AssigneeTemp(3, "신동빈"),
    AssigneeTemp(4, "김수연"),
  )

  private val _teams: MutableStateFlow<MutableList<AssigneeTemp>> = MutableStateFlow(tempAssign)
  val teams: MutableStateFlow<MutableList<AssigneeTemp>>
    get() = _teams

  enum class CurrentState {
    REMAIN,
    DONE
  }
}