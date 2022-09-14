package com.depromeet.housekeeper.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.data.repository.MainRepository
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.AssigneeSelect
import com.depromeet.housekeeper.model.request.UpdateChoreBody
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.model.response.HouseWorks
import com.depromeet.housekeeper.util.DATE_UTIL_TAG
import com.depromeet.housekeeper.util.DateUtil.dateFormat
import com.depromeet.housekeeper.util.DateUtil.fullDateFormat
import com.depromeet.housekeeper.util.DateUtil.getLastDate
import com.depromeet.housekeeper.util.MAIN_TAG
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@HiltViewModel
class MainViewModel(
    private val mainRepository: MainRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    init {
        getCompleteHouseWorkNumber()
        getGroupName()
    }

    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError

    /**
     * 캘린더 관련
     */
    private var calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.SUNDAY
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    }

    private val _dayOfWeek: MutableStateFlow<DayOfWeek> =
        MutableStateFlow(DayOfWeek(date = ""))
    val dayOfWeek: StateFlow<DayOfWeek>
        get() = _dayOfWeek

    private var _startDateOfWeek: MutableStateFlow<String> = MutableStateFlow("") // 2022-08-14
    val startDateOfWeek get() = _startDateOfWeek


    /**
     * 집안일 관련
     */
    private val _completeChoreNum: MutableStateFlow<Int> =
        MutableStateFlow(0)
    val completeChoreNum: StateFlow<Int>
        get() = _completeChoreNum

    // 선택 유저 - 날짜의 집안일
    private val _selectHouseWorks: MutableStateFlow<HouseWorks?> = MutableStateFlow(null)
    val selectHouseWorks: StateFlow<HouseWorks?>
        get() = _selectHouseWorks

    // 선택된 멤버의 주간 집안일
    private var _weekendHouseWorks: MutableStateFlow<Map<String, HouseWorks>> = MutableStateFlow(
        mapOf()
    )
    val weekendHouseWorks get() = _weekendHouseWorks

    private var _weekendChoresLeft: MutableStateFlow<MutableMap<String, Int>> = MutableStateFlow(
        mutableMapOf()
    )
    val weekendChoresLeft get() = _weekendChoresLeft

    private val _selectUserId: MutableStateFlow<Int> = MutableStateFlow(PrefsManager.memberId)
    val selectUserId: StateFlow<Int>
        get() = _selectUserId

    private val _userProfiles: MutableStateFlow<MutableList<Assignee>> =
        MutableStateFlow(mutableListOf())

    private val _rule: MutableStateFlow<String> = MutableStateFlow("")
    val rule: StateFlow<String>
        get() = _rule

    private val _groupName: MutableStateFlow<String> = MutableStateFlow("")
    val groupName: StateFlow<String>
        get() = _groupName

    private val _groups: MutableStateFlow<List<AssigneeSelect>> = MutableStateFlow(listOf())
    val groups: MutableStateFlow<List<AssigneeSelect>>
        get() = _groups


    fun getDatePickerWeek(year: Int, month: Int, dayOfMonth: Int): MutableList<DayOfWeek> {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val selectDate = fullDateFormat.format(calendar.time)
        _dayOfWeek.value = DayOfWeek(selectDate, true)

        calendar.firstDayOfWeek = Calendar.SUNDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val days = mutableListOf<String>()
        days.add(fullDateFormat.format(calendar.time))
        repeat(6) {
            calendar.add(Calendar.DATE, 1)
            days.add(fullDateFormat.format(calendar.time))
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
        Timber.d("selectDate : $date")
    }

    fun updateStartDateOfWeek(date: String) {
        Timber.d("startDate : $date")
        if (startDateOfWeek.value != date) {
            _startDateOfWeek.value = date

        }
    }

    fun getNextWeek(): MutableList<DayOfWeek> {
        var localDate = LocalDate.parse(startDateOfWeek.value)
        localDate = localDate.plusDays(7)
        Timber.d("$DATE_UTIL_TAG getNextWeek : ${localDate}")
        updateStartDateOfWeek(localDate.toString())
        return getWeek()
    }

    fun getLastWeek(): MutableList<DayOfWeek> {
        var localDate = LocalDate.parse(startDateOfWeek.value)
        localDate = localDate.minusDays(7)
        Timber.d("$DATE_UTIL_TAG getLastWeek : ${localDate}")
        updateStartDateOfWeek(localDate.toString())
        return getWeek()
    }

    private fun getWeek(): MutableList<DayOfWeek> {
        val days = mutableListOf<String>()
        val date = dateFormat.parse(startDateOfWeek.value)
        calendar.time = date
        repeat(7) {
            days.add(fullDateFormat.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        Timber.d("$DATE_UTIL_TAG : getWeek : ${days}")
        updateSelectDate(DayOfWeek(date = days[0]))
        updateStartDateOfWeek(days[0].substring(0, 10))
        return days.map { DayOfWeek(date = it, isSelect = it == days[0]) }
            .toMutableList()
    }


    /**
     * Network Communication
     */

    fun getHouseWorks() {
        val fromDate = startDateOfWeek.value
        val toDate = getLastDate(fromDate)

        Timber.d("$MAIN_TAG getHouseWorks $fromDate : ${toDate} : ${selectUserId.value}")
        viewModelScope.launch {
            mainRepository.getPeriodHouseWorkListOfMember(
                selectUserId.value,
                fromDate,
                toDate
            )
                .runCatching {
                    collect {
                        _weekendHouseWorks.value = it.toSortedMap()

                        val choreLeftMap = mutableMapOf<String, Int>()
                        it.toSortedMap().forEach { item ->
                            val scheduledDate = item.key
                            val countLeft = item.value.countLeft
                            choreLeftMap[scheduledDate] = countLeft
                        }
                        weekendChoresLeft.update { choreLeftMap }


                        Timber.d("${it[dayOfWeek.value.date.substring(0, 10)]}")
                        _selectHouseWorks.value = it[dayOfWeek.value.date.substring(0, 10)]
                    }
                }.onFailure {
                    Timber.e(it)
                    _networkError.value = true
                }

        }
        getCompleteHouseWorkNumber()
    }


    fun updateSelectHouseWork(date: String) { // date ex) 2022-10-19
        _selectHouseWorks.value = weekendHouseWorks.value[date]
    }

    //이번주에 끝낸 집안일
    private fun getCompleteHouseWorkNumber() {
        val requestFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        viewModelScope.launch {
            mainRepository.getCompletedHouseWorkNumber(requestFormat.format(Calendar.getInstance().time))
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
            mainRepository.updateChoreState(
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

    fun getRules() {
        viewModelScope.launch {
            mainRepository.getRules()
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
            mainRepository.getDetailHouseWorks(houseWorkId).runCatching {
                collect {
                    _userProfiles.value = it.assignees.toMutableList()
                }
            }.onFailure {
            }
        }
    }

    fun getGroupName() {
        viewModelScope.launch {
            userRepository.getTeam().runCatching {
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
}