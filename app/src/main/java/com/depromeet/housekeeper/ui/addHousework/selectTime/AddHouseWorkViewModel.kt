package com.depromeet.housekeeper.ui.addHousework.selectTime

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.MainRepository
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.request.Chore
import com.depromeet.housekeeper.model.request.RepeatCycle
import com.depromeet.housekeeper.model.request.WeekDays
import com.depromeet.housekeeper.ui.add.RepeatDateImpl
import com.depromeet.housekeeper.util.DateUtil.getFullDate
import com.depromeet.housekeeper.util.PrefsManager
import com.depromeet.housekeeper.util.spaceNameMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddHouseWorkViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val userRepository: UserRepository,
    private val repeatDate: RepeatDateImpl
) : BaseViewModel() {

    private val _curDate: MutableStateFlow<String> = MutableStateFlow("")

    private val _curSpace: MutableStateFlow<String> = MutableStateFlow("방")

    private val _allGroupInfo: MutableStateFlow<ArrayList<Assignee>> =
        MutableStateFlow(arrayListOf())
    val allGroupInfo: StateFlow<ArrayList<Assignee>> get() = _allGroupInfo

    private val _curAssignees: MutableStateFlow<ArrayList<Assignee>> =
        MutableStateFlow(arrayListOf())
    val curAssignees: StateFlow<ArrayList<Assignee>> get() = _curAssignees

    private val _curTime: MutableStateFlow<String?> = MutableStateFlow(null)
    val curTime: StateFlow<String?> get() = _curTime

    private val _positions: MutableStateFlow<ArrayList<Int>> = MutableStateFlow(arrayListOf(0))
    val position: StateFlow<ArrayList<Int>>
        get() = _positions


    private val _chores: MutableStateFlow<ArrayList<Chore>> = MutableStateFlow(arrayListOf())
    val chores: StateFlow<ArrayList<Chore>> get() = _chores

    private val calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    private val _selectCalendar: MutableStateFlow<String> = MutableStateFlow("")
    val selectCalendar: StateFlow<String> get() = _selectCalendar

    private var _selectedDayList: MutableList<WeekDays> = mutableListOf()
    val selectedDayList get() = _selectedDayList

    private var _createdSuccess = MutableStateFlow(false)
    val createdSuccess get() = _createdSuccess

    init {
        setGroupInfo()
    }

    fun setDate(date: String) {
        val lastIndex = date.indexOfLast { it == '-' }
        val requestDate = date.dropLast(date.length - lastIndex)
        _curDate.value = requestDate
    }

    fun bindingSpace(): String {
        return spaceNameMapper(_curSpace.value)
    }

    fun updateSpace(space: String) {
        _curSpace.value = space
    }

    fun getSpace(): String {
        return _curSpace.value
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

    fun setCurAssignees(assignees: ArrayList<Assignee>) {
        _curAssignees.value = assignees
    }

    fun getCurAssignees(): ArrayList<Assignee> {
        return _curAssignees.value
    }

    fun updateAssigneeId(position: Int) {
        val assigneeIds: ArrayList<Int> = arrayListOf()
        _curAssignees.value.map {
            assigneeIds.add(it.memberId)
        }
        _chores.value[position].assignees = assigneeIds
    }


    fun updateTime(hour: Int, min: Int) {
        _curTime.value = "${String.format("%02d", hour)}:${String.format("%02d", min)}"
    }

    fun setTimeNull() {
        _curTime.value = null
    }


    fun updatePositions(position: Int) {
        _positions.value.add(position)
    }

    fun getPosition(type: PositionType): Int = when (type) {
        PositionType.CUR -> _positions.value[_positions.value.size - 1]
        PositionType.PRE -> _positions.value[_positions.value.size - 2]
    }

    fun getRepeatDays(selectedDays: Array<Boolean>): List<WeekDays> {
        val dayList = repeatDate.getRepeatDays(selectedDays)
        if (dayList.isNotEmpty()) _selectedDayList = dayList as MutableList<WeekDays>
        else _selectedDayList = mutableListOf()
        return dayList
    }

    fun getRepeatDaysList(type: String): List<String> =
        repeatDate.getRepeatDaysList(type, selectedDayList)

    fun getRepeatDaysString(type: String, end: String, start: String? = ""): String =
        start + repeatDate.getRepeatDaysString(getRepeatDaysList(type), end)

    fun updateRepeatInform(repeatCycle: RepeatCycle, dayList: List<String>? = null) {
        if (repeatCycle == RepeatCycle.ONCE) return

        val pos = getPosition(PositionType.CUR)
        val repeatPattern: String =
            if (repeatCycle == RepeatCycle.MONTHLY) getCurDay("")
            else repeatDate.getRepeatDaysString(dayList!!)

        _chores.value[pos] = repeatDate.updateRepeatInform(
            cycle = repeatCycle,
            chore = chores.value[pos],
            pattern = repeatPattern
        )
    }

    fun initChores(space: String, choreName: List<String>) {
        val temp = arrayListOf<Chore>()
        choreName.map { name ->
            val chore = Chore()
            chore.apply {
                scheduledDate = _curDate.value
                this.space = space.uppercase()
                houseWorkName = name
                repeatCycle = RepeatCycle.ONCE.value
                repeatPattern = _curDate.value
                assignees = arrayListOf(PrefsManager.memberId)
            }
            temp.add(chore)
        }
        _chores.value.addAll(temp)
    }

    fun updateChoreTime(time: String?, position: Int) {
        _chores.value[position].scheduledTime = time
    }

    fun getChore(position: Int): Chore {
        return _chores.value[position]
    }

    fun updateChoreDate() {
        _chores.value.map { chore ->
            chore.scheduledDate = _curDate.value
        }
    }

    fun getChores(): ArrayList<Chore> {
        return chores.value
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


    fun bindingDate(): String {
        // yyyy-mm-dd-eee
        setDate(selectCalendar.value)
        val str = selectCalendar.value.split("-")
        return "${str[0]}년 ${str[1]}월 ${str[2]}일"
    }

    fun getCurDay(lastWord: String): String {
        val str = _curDate.value.split("-")
        return "${str[2]}$lastWord"
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


    /**
     * Network Communication
     */

    private fun setGroupInfo() {
        viewModelScope.launch {
            userRepository.getTeam().collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    _allGroupInfo.value = sortAssignees(result.members as ArrayList<Assignee>)

                    // 초기에 "나"만 들어가도록 수정
                    setCurAssignees(arrayListOf(getMyInfo()!!))
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
}

enum class PositionType {
    CUR, PRE
}