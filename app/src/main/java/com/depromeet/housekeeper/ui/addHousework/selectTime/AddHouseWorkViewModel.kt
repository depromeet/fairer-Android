package com.depromeet.housekeeper.ui.addHousework.selectTime

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.MainRepository
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.model.request.Chore
import com.depromeet.housekeeper.model.request.Chores
import com.depromeet.housekeeper.model.request.RepeatCycle
import com.depromeet.housekeeper.model.request.WeekDays
import com.depromeet.housekeeper.util.PrefsManager
import com.depromeet.housekeeper.util.spaceNameMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddHouseWorkViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _curDate: MutableStateFlow<String> = MutableStateFlow("")
    val curDate get() = _curDate

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
    val positions: StateFlow<ArrayList<Int>> get() = _positions

    private val _chores: MutableStateFlow<ArrayList<Chore>> = MutableStateFlow(arrayListOf())
    val chores: StateFlow<ArrayList<Chore>> get() = _chores

    private val calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    private val _selectCalendar: MutableStateFlow<String> = MutableStateFlow("")
    val selectCalendar: StateFlow<String> get() = _selectCalendar


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


    fun updatePositions(position: Int) {
        _positions.value.add(position)
    }

    fun getPosition(type: PositionType): Int {
        return when (type) {
            PositionType.CUR -> positions.value[positions.value.size - 1]
            PositionType.PRE -> positions.value[positions.value.size - 2]
        }
    }

    fun updateRepeatDays(pos: Int, selectedDays: Array<Boolean>) {
        val dayList = mutableListOf<String>()
        for (i in 0.. selectedDays.size){
            if (!selectedDays[i]) continue
            val day: WeekDays = when (i) {
                0 -> WeekDays.MON
                1 -> WeekDays.TUE
                2 -> WeekDays.WED
                3 -> WeekDays.THR
                4 -> WeekDays.FRI
                5 -> WeekDays.SAT
                6 -> WeekDays.SUN
                else -> {WeekDays.NONE}
            }
            dayList.add(day.value)
        }
        _chores.value[pos].repeatCycle = if (dayList.size == 7) RepeatCycle.DAYILY.value else RepeatCycle.WEEKLY.value
        _chores.value[pos].repeatPattern = dayList.joinToString(",")
    }

    fun initChores(space: String, choreName: List<String>) {
        val temp = arrayListOf<Chore>()
        choreName.map { name ->
            val chore = Chore()
            chore.apply {
                scheduledDate = curDate.value
                this.space = space.uppercase()
                houseWorkName = name
                repeatCycle = RepeatCycle.ONCE.value
                repeatPattern = curDate.value
                assignees = arrayListOf(PrefsManager.memberId)
            }
            temp.add(chore)
        }
        _chores.value.addAll(temp)
    }

    fun updateChore(time: String?, position: Int) {
        _chores.value[position].scheduledTime = time
    }

    fun getChore(position: Int): Chore {
        return _chores.value[position]
    }

    fun getChores(): ArrayList<Chore> {
        return chores.value
    }

    fun updateChoreDate() {
        _chores.value.map { chore ->
            chore.scheduledDate = curDate.value
        }
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


    fun bindingDate(): String {
        // yyyy-mm-dd-eee
        setDate(_selectCalendar.value)
        val str = _selectCalendar.value.split("-")
        return "${str[0]}년 ${str[1]}월 ${str[2]}일"
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
            mainRepository.createHouseWorks(Chores(chores.value))
                .collectLatest {
                    val result = receiveApiResult(it)
                    result?.houseWorks?.forEach {
                        if (!it.success) setNetworkError(true)
                    }
                }
        }
    }
}

enum class PositionType {
    CUR, PRE
}