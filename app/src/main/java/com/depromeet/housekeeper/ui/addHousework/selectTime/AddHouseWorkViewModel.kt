package com.depromeet.housekeeper.ui.addHousework.selectTime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.data.repository.Repository
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.request.Chore
import com.depromeet.housekeeper.model.request.Chores
import com.depromeet.housekeeper.model.response.HouseWork
import com.depromeet.housekeeper.util.PrefsManager
import com.depromeet.housekeeper.util.dayMapper
import com.depromeet.housekeeper.util.spaceNameMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AddHouseWorkViewModel : ViewModel() {

    init {
        setGroupInfo()
    }

    private val _curDate: MutableStateFlow<String> =
        MutableStateFlow("")

    fun setDate(date: String) {
        val lastIndex = date.indexOfLast { it == '-' }
        val requestDate = date.dropLast(date.length - lastIndex)
        _curDate.value = requestDate
    }

    private val _curSpace: MutableStateFlow<String> =
        MutableStateFlow("방")

    fun bindingSpace(): String {
        return spaceNameMapper(_curSpace.value)
    }

    fun updateSpace(space: String) {
        _curSpace.value = space
    }

    fun getSpace(): String {
        return _curSpace.value
    }

    private val _allGroupInfo: MutableStateFlow<ArrayList<Assignee>> =
        MutableStateFlow(arrayListOf())
    val allGroupInfo: StateFlow<ArrayList<Assignee>>
        get() = _allGroupInfo

    private fun getMyInfo(): Assignee? {
        var temp: Assignee? = null
        _allGroupInfo.value.map {
            if (it.memberId == PrefsManager.memberId) {
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

    private val _curTime: MutableStateFlow<String?> =
        MutableStateFlow(null)
    val curTime: StateFlow<String?>
        get() = _curTime

    fun updateTime(hour: Int, min: Int) {
        _curTime.value = "${String.format("%02d", hour)}:${String.format("%02d", min)}"
    }

    private val _positions: MutableStateFlow<ArrayList<Int>> =
        MutableStateFlow(arrayListOf(0))
    val positions: StateFlow<ArrayList<Int>>
        get() = _positions

    fun updatePositions(position: Int) {
        _positions.value.add(position)
    }

    fun getPosition(type: PositionType): Int {
        return when (type) {
            PositionType.CUR -> _positions.value[_positions.value.size - 1]
            PositionType.PRE -> _positions.value[_positions.value.size - 2]
        }
    }

    private val _chores: MutableStateFlow<ArrayList<Chore>> =
        MutableStateFlow(arrayListOf())
    val chores: StateFlow<ArrayList<Chore>>
        get() = _chores

    fun initChores(space: String, choreName: List<String>) {
        val temp = arrayListOf<Chore>()
        choreName.map { name ->
            val chore = Chore()
            chore.apply {
                scheduledDate = _curDate.value
                this.space = space.uppercase()
                houseWorkName = name
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
        return _chores.value
    }

    fun updateChoreDate() {
        _chores.value.map { chore ->
            chore.scheduledDate = _curDate.value
        }
    }

    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError

    private val _houseWorkCreateResponse: MutableStateFlow<List<HouseWork>?> =
        MutableStateFlow(null)
    val houseWorkCreateResponse: StateFlow<List<HouseWork>?>
        get() = _houseWorkCreateResponse

    private fun setGroupInfo() {
        viewModelScope.launch {
            Repository.getTeam().runCatching {
                collect {
                    _allGroupInfo.value = sortAssignees(it.members as ArrayList<Assignee>)

                    // 초기에 "나"만 들어가도록 수정
                    setCurAssignees(arrayListOf(getMyInfo()!!))
                }
            }
        }
    }

    fun createHouseWorks() {
        viewModelScope.launch {
            Repository.createHouseWorks(Chores(_chores.value))
                .runCatching {
                    collect {
                        Timber.d(it.houseWorks.toString())
                        _houseWorkCreateResponse.value = it.houseWorks
                    }
                }.onFailure {
                    _networkError.value = true
                }
        }
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
        _selectCalendar.value =
            SimpleDateFormat(datePattern, Locale.getDefault()).format(calendar.time)
    }


    fun bindingDate(): String {
        // yyyy-mm-dd-eee
        val str = _selectCalendar.value.split("-")
        val day = dayMapper(str[3])
        setDate(_selectCalendar.value)
        return "${str[0]}년 ${str[1]}월 ${str[2]}일 $day"
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
}

enum class PositionType {
    CUR, PRE
}