package com.depromeet.housekeeper.ui.addDirectTodo

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.MainRepository
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.model.request.*
import com.depromeet.housekeeper.model.response.HouseWork
import com.depromeet.housekeeper.ui.add.RepeatDateImpl
import com.depromeet.housekeeper.util.DateUtil.fullDateParsingToDate
import com.depromeet.housekeeper.util.DateUtil.getFullDate
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddDirectTodoViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val userRepository: UserRepository,
    private val repeatDate: RepeatDateImpl
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
    private val curDate: StateFlow<String> // yyyy-MM-dd
        get() = _curDate

    private val _curTime: MutableStateFlow<String?> =
        MutableStateFlow(null)
    val curTime: StateFlow<String?>
        get() = _curTime

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

    private var _createdSuccess = MutableStateFlow(false)
    val createdSucess get() = _createdSuccess

    private var _isOriginEditChoreRepeat : Boolean = false // 건들면 안됨
    val isOriginEditChoreRepeat get() = _isOriginEditChoreRepeat

    init {
        setGroupInfo()
    }

    fun initDirectChore() {
        _chores.value[0].assignees = arrayListOf(PrefsManager.memberId)
        _chores.value[0].scheduledDate = curDate.value
        _chores.value[0].repeatPattern = curDate.value
        _chores.value[0].space = Chore.ETC_SPACE
    }

    fun initEditChore(houseWork: HouseWork) {
        Timber.d("curDate: ${curDate.value}")
        val assignList: ArrayList<Int> = arrayListOf()
        houseWork.assignees.forEach {
            assignList.add(it.memberId)
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
                updateStandardDate = curDate.value
            )
            _editChore.value = nEditChore
        }

        _curTime.value = houseWork.scheduledTime
        setCurAssignees(houseWork.assignees as ArrayList<Assignee>)

        _isOriginEditChoreRepeat = true

        Timber.d("editChore = ${editChore.value}")
    }

    fun setSelectedDayList(repeatPattern: String) {
        val arr = repeatPattern.split(",")
        _selectedDayList.clear()
        arr.forEach {
            WeekDays.values().find { value -> it == value.eng }?.let {
                _selectedDayList.add(it)
            }
        }
    }

    fun getRepeatDays(selectedDays: Array<Boolean>): List<WeekDays> {
        val dayList = repeatDate.getRepeatDays(selectedDays)
        if (dayList.isNotEmpty()) {
            _selectedDayList = dayList as MutableList<WeekDays>
            return dayList
        }
        _selectedDayList = mutableListOf()
        return listOf()
    }

    fun getRepeatDaysList(type: String): List<String> =
        repeatDate.getRepeatDaysList(type, selectedDayList)

    fun getRepeatDaysString(type: String, end: String, start: String? = ""): String =
        start + repeatDate.getRepeatDaysString(getRepeatDaysList(type), end)

    fun updateRepeatInform(repeatCycle: RepeatCycle, dayList: List<String>? = null) {
        if (curViewType.value == ViewType.EDIT && editChore.value != null) { // 수정
            repeatInform(editChore.value!!, repeatCycle, dayList)
        } else if (curViewType.value == ViewType.ADD) { // 신규 (직접 입력)
            repeatInform(chores.value[0], repeatCycle, dayList)
        }

    }

    private fun repeatInform(
        chore: Any,
        repeatCycle: RepeatCycle,
        dayList: List<String>? = null
    ) {
        val repeatPattern: String =
            if (repeatCycle == RepeatCycle.MONTHLY) getCurDay("")
            else repeatDate.getRepeatDaysString(dayList!!)

        if (chore is EditChore) {
            _editChore.value = repeatDate.updateRepeatInform(
                cycle = repeatCycle,
                editChore = chore,
                pattern = repeatPattern
            )
            Timber.d("smart cast editChore : ${repeatCycle}")
        } else if (chore is Chore) {
            _chores.value[0] = repeatDate.updateRepeatInform(
                cycle = repeatCycle,
                chore = chore,
                pattern = repeatPattern
            )
            Timber.d("smart cast directChore : ${repeatCycle}, ${repeatPattern}")
        }
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

        _selectCalendar.value = calendar.time.getFullDate()
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
        setCurDate(selectCalendar.value)
        val str = selectCalendar.value.split("-")
        return "${str[0]}년 ${str[1]}월 ${str[2]}일"
    }

    fun updateChoreName(viewType: ViewType, name: String) {
        if (viewType == ViewType.ADD) {
            _chores.value[0].houseWorkName = name
        } else if (viewType == ViewType.EDIT) {
            _editChore.value!!.houseWorkName = name
        }
    }

    fun updateTime(hour: Int, min: Int) {
        _curTime.value = "${String.format("%02d", hour)}:${String.format("%02d", min)}"
    }

    fun updateChoreTime(viewType: ViewType, time: String?) {
        if (viewType == ViewType.ADD) {
            _chores.value[0].scheduledTime = time
        } else if (viewType == ViewType.EDIT) {
            _editChore.value!!.scheduledTime = time
        }
    }

    fun updateChoreRepeat(isRepeatChecked: Boolean) {
        if (curViewType.value == ViewType.ADD) {
            if (!isRepeatChecked) {
                _chores.value[0].apply {
                    repeatCycle = RepeatCycle.ONCE.value
                    repeatPattern = curDate.value
                }
            }else if (chores.value[0].repeatCycle == RepeatCycle.WEEKLY.value && chores.value[0].repeatPattern.isEmpty()) {
                _chores.value[0].repeatCycle = RepeatCycle.ONCE.value
            }
        } else if (curViewType.value == ViewType.EDIT && editChore.value != null) {
            if (!isRepeatChecked) {
                _editChore.value!!.apply {
                    repeatCycle = RepeatCycle.ONCE.value
                    repeatPattern = curDate.value
                }
            } else if (editChore.value!!.repeatCycle == RepeatCycle.WEEKLY.value && editChore.value!!.repeatPattern.isNullOrEmpty()) {
                _editChore.value!!.repeatCycle = RepeatCycle.ONCE.value
            }
        }
    }

    fun updateAssigneeId(viewType: ViewType) {
        val assigneeIds: ArrayList<Int> = arrayListOf()
        _curAssignees.value.map {
            assigneeIds.add(it.memberId)
        }
        if (viewType == ViewType.ADD) {
            _chores.value[0].assignees = assigneeIds
        } else if (viewType == ViewType.EDIT) {
            _editChore.value!!.assignees = assigneeIds
        }
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

    fun setCurDate(date: String) {
        if (date.fullDateParsingToDate() != null)
            _curDate.value = date.fullDateParsingToDate()!!
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

    fun createHouseWorks() {
        viewModelScope.launch {
            mainRepository.createHouseWorks(chores.value)
                .collectLatest {
                    val result = receiveApiResult(it)
                    if (result.isNullOrEmpty()) {
                        setNetworkError(true)
                        return@collectLatest
                    }

                    _createdSuccess.value = true

                }
        }
    }


    fun deleteHouseWork(editType: EditType) {
        viewModelScope.launch {
            mainRepository.deleteHouseWork(
                DeleteChoreRequest(
                    deleteStandardDate = curDate.value,
                    houseWorkId = houseWorkId.value,
                    type = editType.value
                )
            ).collectLatest {
                receiveApiResult(it)
            }
        }
    }

    fun editHouseWork(type: EditType) {
        viewModelScope.launch {
            if (editChore.value == null) return@launch
            _editChore.value!!.type = type.value
            if (type.value == EditType.ONLY.value) {
                _editChore.value!!.repeatPattern = editChore.value!!.scheduledDate
            }
            mainRepository.editHouseWork(editChore.value!!)
                .collectLatest {
                    receiveApiResult(it)
                }
        }
    }

}
