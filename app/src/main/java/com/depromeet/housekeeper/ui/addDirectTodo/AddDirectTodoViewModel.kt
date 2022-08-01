package com.depromeet.housekeeper.ui.addDirectTodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.util.PrefsManager
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.data.repository.Repository
import com.depromeet.housekeeper.util.dayMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddDirectTodoViewModel : ViewModel() {

  init {
    setGroupInfo()
  }

  private val _curViewType: MutableStateFlow<ViewType> =
    MutableStateFlow(ViewType.ADD)
  val curViewType: StateFlow<ViewType>
    get() = _curViewType

  fun setViewType(viewType: ViewType) {
    _curViewType.value = viewType
  }

  private val _houseWorkId: MutableStateFlow<Int> = MutableStateFlow(-1)
  val houseWorkId: StateFlow<Int>
    get() = _houseWorkId

  fun setHouseWorkId(id: Int) {
    _houseWorkId.value = id
  }

  private val _curDate: MutableStateFlow<String> =
    MutableStateFlow("")
  val curDate: StateFlow<String>
    get() = _curDate

  fun setDate(date: String) {
    val lastIndex = date.indexOfLast { it == '-' }
    val requestDate = date.dropLast(date.length - lastIndex)
    _curDate.value = requestDate
  }

  fun updateChoreDate() {
    _chores.value[0].scheduledDate = _curDate.value
  }

  private val _curChoreName: MutableStateFlow<String> =
    MutableStateFlow("")
  val curChoreName: StateFlow<String?>
    get() = _curChoreName

  fun updateChoreName(name: String) {
    _chores.value[0].houseWorkName = name
  }

  private val _curTime: MutableStateFlow<String?> =
    MutableStateFlow(null)
  val curTime: StateFlow<String?>
    get() = _curTime

  fun updateTime(hour: Int, min: Int) {
    _curTime.value = "${String.format("%02d", hour)}:${String.format("%02d", min)}"
  }

  fun updateChoreTime(time: String?) {
    _chores.value[0].scheduledTime = time
  }

  private val _curSpace: MutableStateFlow<String> =
    MutableStateFlow(Chore.ETC_SPACE) // ETC
  val curSpace: StateFlow<String>
    get() = _curSpace

  val _allGroupInfo: MutableStateFlow<ArrayList<Assignee>> =
    MutableStateFlow(arrayListOf())
  val allGroupInfo: StateFlow<ArrayList<Assignee>>
    get() = _allGroupInfo

  private fun getMyInfo(): Assignee? {
    var temp: Assignee? = null
    _allGroupInfo.value.map {
      if(it.memberId == PrefsManager.memberId) {
        temp = it
      }
    }
    return temp
  }

  private val _curAssignees: MutableStateFlow<ArrayList<Assignee>> =
    MutableStateFlow(arrayListOf())
  val curAssignees: StateFlow<ArrayList<Assignee>>
    get() = _curAssignees

  fun setCurAssignees(assignees: ArrayList<Assignee>) {
    _curAssignees.value = assignees
  }

  fun getCurAssignees() : ArrayList<Assignee> {
    return _curAssignees.value
  }

  fun updateAssigneeId() {
    val assigneeIds: ArrayList<Int> = arrayListOf()
    _curAssignees.value.map {
      assigneeIds.add(it.memberId)
    }
     _chores.value[0].assignees = assigneeIds
  }

  // 직접 추가 or 수정은 chore 개수 1
  private val _chores: MutableStateFlow<ArrayList<Chore>> =
    MutableStateFlow(arrayListOf(Chore()))
  val chores: StateFlow<ArrayList<Chore>>
    get() = _chores

  fun initDirectChore() {
    _chores.value[0].assignees = arrayListOf(PrefsManager.memberId)
    _chores.value[0].scheduledDate = _curDate.value
    _chores.value[0].space = Chore.ETC_SPACE
  }

  fun initEditChore(chore: Chore, curAssignees: List<Assignee>) {
    // main에서 받아온 집안일 정보 init
    _curDate.value = chore.scheduledDate
    _curTime.value = chore.scheduledTime

    _chores.value[0].scheduledDate = chore.scheduledDate
    _chores.value[0].houseWorkName = chore.houseWorkName
    _chores.value[0].scheduledTime = chore.scheduledTime
    _chores.value[0].space = chore.space
    _chores.value[0].assignees = chore.assignees

    setCurAssignees(curAssignees as ArrayList<Assignee>)
  }


  private val calendar: Calendar = Calendar.getInstance().apply {
    set(Calendar.MONTH, this.get(Calendar.MONTH))
    firstDayOfWeek = Calendar.MONDAY
    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
  }

  private val _selectCalendar: MutableStateFlow<String> = MutableStateFlow("")
  val selectCalendar: StateFlow<String>
    get() = _selectCalendar

  fun addCalendarView(selectDate: String) {
    _selectCalendar.value = selectDate
  }

  fun updateCalendarView(year: Int, month: Int, dayOfMonth: Int) {
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

    val datePattern = "yyyy-MM-dd-EEE"
    _selectCalendar.value = SimpleDateFormat(datePattern, Locale.getDefault()).format(calendar.time)
  }

  private fun sortAssignees(allAssignees: ArrayList<Assignee>): ArrayList<Assignee> {
    val temp = arrayListOf<Assignee>()
    allAssignees.map { assignee ->
      if(assignee.memberId == PrefsManager.memberId) {
        temp.add(0, assignee)
      }
      else {
        temp.add(assignee)
      }
    }
    return temp
  }

  // TODO: 팀 조회 API에서 members 정보만 GET
  private fun setGroupInfo() {
    viewModelScope.launch {
      Repository.getTeam().runCatching {
        collect {
          _allGroupInfo.value = sortAssignees(it.members as ArrayList<Assignee>)

          // 직접 추가 뷰라면 "나" 자신 담당자 추가
          if(_curViewType.value == ViewType.ADD) {
            setCurAssignees(arrayListOf(getMyInfo()!!))
          }
        }
      }
    }
  }

  // 집안일 직접 추가 api
  fun createHouseWorks() {
    viewModelScope.launch {
      Repository.createHouseWorks(Chores(_chores.value))
        .runCatching {
          collect {
          }
        }.onFailure {
          _networkError.value = true
        }
    }
  }

  fun editHouseWork() {
    viewModelScope.launch {
      Repository.editHouseWork(houseWorkId.value, _chores.value[0])
        .runCatching {
          collect {
            Timber.d(it.toString())
          }
        }.onFailure {
          _networkError.value = true
        }
    }
  }

  private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val networkError: StateFlow<Boolean>
    get() = _networkError

  fun deleteHouseWork() {
    viewModelScope.launch {
      Repository.deleteHouseWork(houseWorkId.value)
        .runCatching {
          collect {

          }
      }.onFailure {
        _networkError.value = true
      }
    }
  }

  fun bindingDate(): String {
    // yyyy-mm-dd-eee
    setDate(_selectCalendar.value)
    val str = _selectCalendar.value.split("-")
    val day = dayMapper(str[3])
    return "${str[0]}년 ${str[1]}월 ${str[2]}일 $day"
  }
}
