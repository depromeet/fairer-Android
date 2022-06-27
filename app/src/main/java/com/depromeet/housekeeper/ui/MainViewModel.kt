package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.AssigneeSelect
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
  val format = SimpleDateFormat(datePattern, Locale.getDefault())

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

  fun getToday(): DayOfWeek {
    return DayOfWeek(
      date = format.format(Date(System.currentTimeMillis())),
      isSelect = true
    )
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
    return days.map { DayOfWeek(date = it, isSelect = it == dayOfWeek.value.date) }.toMutableList()
  }

  private val _completeChoreNum: MutableStateFlow<Int> =
    MutableStateFlow(0)
  val completeChoreNum: StateFlow<Int>
    get() = _completeChoreNum

  private val _selectHouseWorks: MutableStateFlow<HouseWorks?> = MutableStateFlow(null)
  val selectHouseWork: StateFlow<HouseWorks?>
    get() = _selectHouseWorks

  private val _allHouseWorks: MutableStateFlow<List<HouseWorks>> = MutableStateFlow(listOf())

  private val _currentState: MutableStateFlow<CurrentState> = MutableStateFlow(CurrentState.REMAIN)
  val currentState: StateFlow<CurrentState>
    get() = _currentState

  private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val networkError: StateFlow<Boolean>
    get() = _networkError

  private val _selectUserId: MutableStateFlow<Int> = MutableStateFlow(PrefsManager.memberId)
  val selectUserId: StateFlow<Int>
    get() = _selectUserId

  private val _userProfiles: MutableStateFlow<MutableList<Assignee>> =
    MutableStateFlow(mutableListOf())
  val userProfiles: StateFlow<MutableList<Assignee>>
    get() = _userProfiles

  fun getHouseWorks() {
    //TODO 성능 개선 필요
    val dayOfWeekDate = dayOfWeek.value.date
    val lastIndex = dayOfWeekDate.indexOfLast { it == '-' }
    val requestDate = dayOfWeekDate.dropLast(dayOfWeekDate.length - lastIndex)
    viewModelScope.launch {
      Repository.getList(requestDate)
        .runCatching {
          collect {
            _allHouseWorks.value = it
            _selectHouseWorks.value =
              _allHouseWorks.value.find { it.memberId == _selectUserId.value }
          }
        }.onFailure {
          //  _networkError.value = true
        }
    }
    getCompleteHouseWorkNumber()
  }

  fun updateSelectHouseWork(selectUser: Int) {
    _selectHouseWorks.value = _allHouseWorks.value.find { it.memberId == selectUser }
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

  private val _groups: MutableStateFlow<List<AssigneeSelect>> = MutableStateFlow(listOf())
  val groups: MutableStateFlow<List<AssigneeSelect>>
    get() = _groups

  fun getGroupName() {
    viewModelScope.launch {
      Repository.getTeam().runCatching {
        collect {

          val groupSize: Int = it.members.size
          _groupName.value = "${it.teamName} $groupSize"

          val myAssignee = it.members.find { it.memberId == PrefsManager.memberId }!!
          val assignees = listOf(myAssignee) + it.members

          _groups.value = assignees.distinct().map {
            AssigneeSelect(
              it.memberId,
              it.memberName,
              it.profilePath,
              it.memberId == selectUserId.value
            )
          }
        }
      }
    }
  }

  fun updateSelectUser(selectUser: Int) {
    _selectUserId.value = selectUser

    val newGroups = _groups.value.map {
      AssigneeSelect(
        it.memberId,
        it.memberName,
        it.profilePath,
        it.memberId == selectUser
      )
    }
    _groups.value = newGroups
  }


  private val _rule: MutableStateFlow<String> = MutableStateFlow("")
  val rule: StateFlow<String>
    get() = _rule

  fun getRules() {
    viewModelScope.launch {
      Repository.getRules()
        .runCatching {
          collect {
            _rule.value = when {
              it.ruleResponseDtos.isNotEmpty() -> it.ruleResponseDtos.random().ruleName
              else -> ""
            }
          }
        }
    }
  }

  fun getDetailHouseWork(houseWorkId: Int) {
    viewModelScope.launch {
      Repository.getDetailHouseWorks(houseWorkId).runCatching {
        collect {
          _userProfiles.value = it.assignees.toMutableList()
        }
      }.onFailure {
      }
    }
  }


  enum class CurrentState {
    REMAIN,
    DONE
  }
}