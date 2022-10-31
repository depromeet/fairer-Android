package com.depromeet.housekeeper.ui.addDirectTodo

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.MainRepository
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.model.request.*
import com.depromeet.housekeeper.model.response.HouseWork
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddDirectTodoViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _curViewType: MutableStateFlow<ViewType> =
        MutableStateFlow(ViewType.ADD)
    val curViewType: StateFlow<ViewType>
        get() = _curViewType


    private val _houseWorkId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val houseWorkId: StateFlow<Int>
        get() = _houseWorkId


    private val _curDate: MutableStateFlow<String> =
        MutableStateFlow("")
    val curDate: StateFlow<String>
        get() = _curDate

    private val _curChoreName: MutableStateFlow<String> =
        MutableStateFlow("")
    val curChoreName: StateFlow<String?>
        get() = _curChoreName

    private val _curTime: MutableStateFlow<String?> =
        MutableStateFlow(null)
    val curTime: StateFlow<String?>
        get() = _curTime

    private val _curSpace: MutableStateFlow<String> =
        MutableStateFlow(Chore.ETC_SPACE) // ETC
    val curSpace: StateFlow<String>
        get() = _curSpace

    val _allGroupInfo: MutableStateFlow<ArrayList<Assignee>> =
        MutableStateFlow(arrayListOf())
    val allGroupInfo: StateFlow<ArrayList<Assignee>>
        get() = _allGroupInfo

    private val _curAssignees: MutableStateFlow<ArrayList<Assignee>> =
        MutableStateFlow(arrayListOf())
    val curAssignees: StateFlow<ArrayList<Assignee>>
        get() = _curAssignees

    // 직접 추가 or 수정은 chore 개수 1
    private val _chores: MutableStateFlow<ArrayList<Chore>> =
        MutableStateFlow(arrayListOf(Chore()))
    val chores: StateFlow<ArrayList<Chore>>
        get() = _chores

    private var _editChore: MutableStateFlow<EditChore?> = MutableStateFlow(null)
    val editChore: StateFlow<EditChore?> get() = _editChore

    private var _selectedDayList: MutableList<WeekDays> = mutableListOf()
    val selectedDayList get() = _selectedDayList

    private val _selectCalendar: MutableStateFlow<String> = MutableStateFlow("")
    val selectCalendar: StateFlow<String>
        get() = _selectCalendar

    private val calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    init {
        setGroupInfo()
    }

    fun initDirectChore() {
        _chores.value[0].assignees = arrayListOf(PrefsManager.memberId)
        _chores.value[0].scheduledDate = _curDate.value
        _chores.value[0].space = Chore.ETC_SPACE
    }

    fun initEditChore(houseWork: HouseWork) {
        val assignList: ArrayList<Int> = arrayListOf()
        houseWork.assignees.map {
            assignList.plus(it.memberId)
        }
        houseWork.apply {
            val nEditChore = EditChore(
                assignees = assignList,
                houseWorkId = houseWorkId,
                houseWorkName = houseWorkName,
                repeatCycle = repeatCycle,
                repeatEndDate = repeatEndDate,
                repeatPattern = repeatPattern,
                scheduledDate = scheduledDate,
                scheduledTime = scheduledTime,
                space = space,
                type = EditType.NONE.value,
                updateStandardDate =scheduledDate
            )
            _editChore.value = nEditChore
        }
        _curDate.value = houseWork.scheduledDate
        _curTime.value = houseWork.scheduledTime
        setCurAssignees(houseWork.assignees as ArrayList<Assignee>)
    }

    fun setSelectedDayList(repeatPattern: String){
        val arr = repeatPattern.split(",")
        _selectedDayList.clear()
        arr.forEach {
            var item = WeekDays.NONE
            when (it){
                WeekDays.MON.eng -> WeekDays.MON
                WeekDays.TUE.eng -> WeekDays.TUE
                WeekDays.WED.eng -> WeekDays.WED
                WeekDays.THR.eng -> WeekDays.THR
                WeekDays.FRI.eng -> WeekDays.FRI
                WeekDays.SAT.eng -> WeekDays.SAT
                WeekDays.SUN.eng -> WeekDays.SUN
            }
            if (item != WeekDays.NONE) _selectedDayList.add(item)
        }
    }

    fun getRepeatDaysString(type: String): MutableList<String> {
        val repeatDaysString = mutableListOf<String>()
        if (type == "kor") {
            selectedDayList.forEach { repeatDaysString.add(it.kor) }
        } else if (type == "eng"){
            selectedDayList.forEach { repeatDaysString.add(it.eng) }
        }
        return repeatDaysString
    }

    fun getCurDay(lastWord: String): String {
        val str = curDate.value.split("-")
        return "${str[2]}$lastWord"
    }

    fun addCalendarView(selectDate: String) {
        _selectCalendar.value = selectDate
    }

    fun updateCalendarView(year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val datePattern = "yyyy-MM-dd-EEE"
        _selectCalendar.value =
            SimpleDateFormat(datePattern, Locale.getDefault()).format(calendar.time)
    }

    private fun sortAssignees(allAssignees: ArrayList<Assignee>): ArrayList<Assignee> {
        val temp = arrayListOf<Assignee>()
        allAssignees.map { assignee ->
            if (assignee.memberId == PrefsManager.memberId) {
                temp.add(0, assignee)
            } else {
                temp.add(assignee)
            }
        }
        return temp
    }


    fun bindingDate(): String {
        // yyyy-mm-dd-eee
        setCurrentDate(_selectCalendar.value)
        val str = _selectCalendar.value.split("-")
        return "${str[0]}년 ${str[1]}월 ${str[2]}일"
    }

    fun updateChoreDate() {
        chores.value[0].scheduledDate = curDate.value
    }

    fun updateChoreName(name: String) {
        _chores.value[0].houseWorkName = name
    }

    fun updateTime(hour: Int, min: Int) {
        _curTime.value = "${String.format("%02d", hour)}:${String.format("%02d", min)}"
    }

    fun updateChoreTime(time: String?) {
        _chores.value[0].scheduledTime = time
    }

    fun updateAssigneeId() {
        val assigneeIds: ArrayList<Int> = arrayListOf()
        _curAssignees.value.map {
            assigneeIds.add(it.memberId)
        }
        _chores.value[0].assignees = assigneeIds
    }

    fun setViewType(viewType: ViewType) {
        _curViewType.value = viewType
    }

    fun setCurAssignees(assignees: ArrayList<Assignee>) {
        _curAssignees.value = assignees
    }

    fun getCurAssignees(): ArrayList<Assignee> {
        return _curAssignees.value
    }

    fun setHouseWorkId(id: Int) {
        _houseWorkId.value = id
    }

    fun setCurrentDate(date: String) {
        val lastIndex = date.indexOfLast { it == '-' }
        val requestDate = date.dropLast(date.length - lastIndex)
        _curDate.value = requestDate
    }

    private fun getMyInfo(): Assignee? {
        var temp: Assignee? = null
        _allGroupInfo.value.map {
            if (it.memberId == PrefsManager.memberId) {
                temp = it
            }
        }
        return temp
    }


    /**
     * Network Communication
     */

    // TODO: 팀 조회 API에서 members 정보만 GET
    private fun setGroupInfo() {
        viewModelScope.launch {
            userRepository.getTeam().collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    _allGroupInfo.value = sortAssignees(result.members as ArrayList<Assignee>)
                    // 직접 추가 뷰라면 "나" 자신 담당자 추가
                    if (_curViewType.value == ViewType.ADD) {
                        setCurAssignees(arrayListOf(getMyInfo()!!))
                    }
                }
            }
        }
    }

    //todo
    // 집안일 직접 추가 api
    fun createHouseWorks() {
        viewModelScope.launch {
            mainRepository.createHouseWorks(_chores.value)
                .collectLatest {
                    val result = receiveApiResult(it)
                    if (result.isNullOrEmpty()) {
                        setNetworkError(true)
                    }
                }
        }
    }


    // todo api 변경후 작업
    fun deleteHouseWork() {
        viewModelScope.launch {
            mainRepository.deleteHouseWork(houseWorkId.value)
                .runCatching {
                    collect {

                    }
                }.onFailure {
                    setNetworkError(true)
                }
        }
    }

    // todo api 변경후 작업
    fun editHouseWork() {
        viewModelScope.launch {
            mainRepository.editHouseWork(houseWorkId.value, _chores.value[0])
                .runCatching {
                    collect {
                        Timber.d(it.toString())
                    }
                }.onFailure {
                    setNetworkError(true)
                }
        }
    }

}
