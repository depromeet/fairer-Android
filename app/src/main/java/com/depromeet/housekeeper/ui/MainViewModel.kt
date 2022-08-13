package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainViewModel : ViewModel() {

    init {
        getCompleteHouseWorkNumber()
        getGroupName()
    }

    //캘린더 관련
    private val todayCalendar: Calendar = Calendar.getInstance()

    private var calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.SUNDAY
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    }

    private val datePattern = "yyyy-MM-dd-EEE"
    val format = SimpleDateFormat(datePattern, Locale.getDefault())

    private val _dayOfWeek: MutableStateFlow<DayOfWeek> =
        MutableStateFlow(DayOfWeek(date = format.format(Date(System.currentTimeMillis()))))
    val dayOfWeek: StateFlow<DayOfWeek>
        get() = _dayOfWeek

    private fun getCurrentWeek(): MutableList<DayOfWeek> {
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
        calendar = todayCalendar.clone() as Calendar
        calendar.apply {
            set(Calendar.MONTH, this.get(Calendar.MONTH))
            firstDayOfWeek = Calendar.SUNDAY
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }
        getCurrentWeek()
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

        calendar.firstDayOfWeek = Calendar.SUNDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

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
        updateSelectDate(DayOfWeek(date = days[0]))
        return days.map { DayOfWeek(date = it, isSelect = it == days[0]) }
            .toMutableList()
    }

    //집안일 관련
    private val _completeChoreNum: MutableStateFlow<Int> =
        MutableStateFlow(0)
    val completeChoreNum: StateFlow<Int>
        get() = _completeChoreNum

    //선택된 유저의 집안일
    private val _selectHouseWorks: MutableStateFlow<HouseWorks?> = MutableStateFlow(null)
    val selectHouseWork: StateFlow<HouseWorks?>
        get() = _selectHouseWorks

    private val _allHouseWorks: MutableStateFlow<List<HouseWorks>> = MutableStateFlow(listOf())

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

    fun getHouseWorksSize(): Int {
        return _allHouseWorks.value.size
    }

    fun getHouseWorks() {
        //TODO 성능 개선 필요
        val fromDate = dayOfWeek.value.date.substring(0, 10)
        val localDate = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val toDate = localDate.plusDays(6)

        Timber.d("$fromDate : ${toDate} : ${selectUserId.value}")
        viewModelScope.launch {
//            Repository.getPeriodHouseWorkListOfMember(selectUserId.value, fromDate, toDate.toString())
//                .runCatching {
//                    collect {
//                        Timber.d("getHouseWorks", it)
//                    }
//                }.onFailure {
//                    Timber.e(it)
//                }

            Repository.getList(fromDate)
                .runCatching {
                    collect {
                        _allHouseWorks.value = it
                        _selectHouseWorks.value =
                            _allHouseWorks.value.find { it.memberId == _selectUserId.value }
                    }
                }.onFailure {
                    _networkError.value = true
                }
        }
        getCompleteHouseWorkNumber()
    }


    fun updateSelectHouseWork(selectUser: Int) {
        _selectHouseWorks.value = _allHouseWorks.value.find { it.memberId == selectUser }
    }

    //이번주에 끝낸 집안일
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


    fun updateChoreState(houseWork: HouseWork) {
        val toBeStatus = when (houseWork.success) {
            false -> 1
            else -> 0
        }
        viewModelScope.launch {
            Repository.updateChoreState(
                houseWorkId = houseWork.houseWorkId,
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
}