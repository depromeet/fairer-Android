package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.network.remote.repository.Repository
import com.depromeet.housekeeper.util.dayMapper
import com.depromeet.housekeeper.util.spaceNameMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddHouseWorkViewModel: ViewModel(){

    init {
        setGroupInfo()
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

    private val _curSpace: MutableStateFlow<String> =
        MutableStateFlow("방")
    val curSpace: StateFlow<String>
        get() = _curSpace

    fun bindingSpace():String {
        return spaceNameMapper(_curSpace.value)
    }

    fun updateSpace(space: String) {
        _curSpace.value = space
    }

    fun getSpace(): String {
        return _curSpace.value
    }

    private val _myInfo: MutableStateFlow<Assignee> =
        MutableStateFlow(Assignee(-1, "", ""))
    val myInfo: StateFlow<Assignee>
        get() = _myInfo

    val _allGroupInfo: MutableStateFlow<ArrayList<Assignee>> =
        MutableStateFlow(arrayListOf())
    val allGroupInfo: StateFlow<ArrayList<Assignee>>
        get() = _allGroupInfo

    fun getAllGroupInfo() : ArrayList<Assignee> {
        return _allGroupInfo.value
    }

    // TODO : Assignee의 List vs memberId의 List
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

    fun getPosition(type: PositionType):Int {
        return when (type) {
            PositionType.CUR -> _positions.value[_positions.value.size - 1]
            PositionType.PRE -> _positions.value[_positions.value.size - 2]
        }
    }

    private val _chores: MutableStateFlow<ArrayList<Chore>> =
        MutableStateFlow(arrayListOf())
    val chores: StateFlow<ArrayList<Chore>>
        get() = _chores

    // TODO: assignee 추가
    fun initChores(space:String, choreName: List<String>) {
        val temp = arrayListOf<Chore>()
        choreName.map{ name ->
            val chore = Chore()
            chore.scheduledDate = _curDate.value
            chore.space = space.uppercase()
            chore.houseWorkName = name
            chore.assignees = arrayListOf(_myInfo.value.memberId)
            temp.add(chore)
        }
        _chores.value.addAll(temp)
    }

    // TODO: assignee 추가
    fun updateChore(time: String?, position: Int) {
        _chores.value[position].scheduledTime = time
    }

    fun getChore(position: Int): Chore {
        return _chores.value[position]
    }

    fun getChores() : ArrayList<Chore>{
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

    private val _houseWorkCreateResponse: MutableStateFlow<List<HouseWork>?> = MutableStateFlow(null)
    val houseWorkCreateResponse: StateFlow<List<HouseWork>?>
        get() = _houseWorkCreateResponse

    // TODO: 팀 조회 API에서 members 정보만 GET
    private fun setGroupInfo() {
        viewModelScope.launch {
            Repository.getTeam().runCatching {
                collect {
                    _allGroupInfo.value = it.members as ArrayList<Assignee>

                    // TODO: 0번 index가 나인걸로 수정
                    _myInfo.value = Assignee(10, "ss", "https://fairer-image.s3.ap-northeast-2.amazonaws.com/fairer-profile-images/Profile-2x-7.png")

                    // 초기에 "나"만 들어가도록 수정
                     setCurAssignees(arrayListOf(_myInfo.value))
                }
            }
        }
    }

    // TODO: assignee 추가
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

    fun addCalendarView(selectDate : String) {
        _selectCalendar.value = selectDate
    }

    fun updateCalendarView(year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val datePattern = "yyyy-MM-dd-EEE"
        _selectCalendar.value = SimpleDateFormat(datePattern, Locale.getDefault()).format(calendar.time)
    }


    fun bindingDate(): String {
        // yyyy-mm-dd-eee
        val str = _selectCalendar.value.split("-")
        val day = dayMapper(str[3])
        setDate(_selectCalendar.value)
        return "${str[0]}년 ${str[1]}월 ${str[2]}일 $day"
    }
}

enum class PositionType {
    CUR, PRE
}