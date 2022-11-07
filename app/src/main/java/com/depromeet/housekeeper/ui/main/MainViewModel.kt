package com.depromeet.housekeeper.ui.main

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.MainRepository
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.AssigneeSelect
import com.depromeet.housekeeper.model.DayOfWeek
import com.depromeet.housekeeper.model.request.UpdateChoreBody
import com.depromeet.housekeeper.model.response.HouseWork
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    init {
        getCompleteHouseWorkNumber()
        getGroupName()
    }

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

    private var _selectedHouseWorkItem: MutableStateFlow<HouseWork?> = MutableStateFlow(null)
    val selectedHouseWorkItem get() = _selectedHouseWorkItem

    private val _selectUserId: MutableStateFlow<Int> = MutableStateFlow(PrefsManager.memberId)
    val selectUserId: StateFlow<Int>
        get() = _selectUserId

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
    fun getSelectWeek():MutableList<DayOfWeek>{
        var localDate = LocalDate.parse(startDateOfWeek.value)
        updateStartDateOfWeek(localDate.toString())
        val days = mutableListOf<String>()
        val date = dateFormat.parse(startDateOfWeek.value)
        calendar.time = date
        repeat(7) {
            days.add(fullDateFormat.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return days.map { DayOfWeek(date = it, isSelect = it == dayOfWeek.value.date) }
            .toMutableList()
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

    fun updateSelectHouseWork(date: String) { // date ex) 2022-10-19
        _selectHouseWorks.value = weekendHouseWorks.value[date]
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

    fun setSelectedHouseWorkItem(houseWork: HouseWork?){
        _selectedHouseWorkItem.value = houseWork
    }

    /**
     * Network Communication
     */

    fun getHouseWorks() {
        val fromDate = startDateOfWeek.value
        val toDate = getLastDate(fromDate)

        Timber.d("$MAIN_TAG getHouseWorks $fromDate : $toDate : ${selectUserId.value}")
        viewModelScope.launch {
            mainRepository.getPeriodHouseWorkListOfMember(selectUserId.value, fromDate, toDate)
                .collectLatest {
                    val result = receiveApiResult(it)
                    if (result != null) {
                        _weekendHouseWorks.value = result.toSortedMap()

                        val choreLeftMap = mutableMapOf<String, Int>()
                        result.toSortedMap().forEach { item ->
                            val scheduledDate = item.key
                            val countLeft = item.value.countLeft
                            choreLeftMap[scheduledDate] = countLeft
                        }
                        weekendChoresLeft.update { choreLeftMap }
                        _selectHouseWorks.value = result[dayOfWeek.value.date.substring(0, 10)]
                        Timber.d("AddDirect ${dayOfWeek.value.date.substring(0, 10)} : ${selectHouseWorks.value}")

                    }
                }
        }
        getCompleteHouseWorkNumber()
    }


    //todo
    //이번주에 끝낸 집안일
    private fun getCompleteHouseWorkNumber() {
        val requestFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        viewModelScope.launch {
            mainRepository.getCompletedHouseWorkNumber(requestFormat.format(Calendar.getInstance().time))
                .collectLatest {
                    val result = receiveApiResult(it)
                    if (result != null) {
                        _completeChoreNum.value = result.count
                    }
                }
        }
    }

    //todo
    fun updateChoreState(houseWork: HouseWork) {
        viewModelScope.launch {
            mainRepository.updateChoreState(
                houseWorkId = houseWork.houseWorkId,
                scheduledDate = houseWork.scheduledDate
            ).runCatching {
                collect {
                    getHouseWorks()
                }
            }.onFailure {
                setNetworkError(true)
            }
        }
    }
    fun updateChoreComplete(houseWork: HouseWork){
        viewModelScope.launch {
            mainRepository.updateChoreComplete(houseWork.houseWorkCompleteId!!).runCatching {
                collect{
                    getHouseWorks()
                }
                }.onFailure {
                    setNetworkError(true)
            }
        }
    }

    fun getDetailHouseWork(houseWorkId: Int) {
        viewModelScope.launch {
            mainRepository.getDetailHouseWorks(houseWorkId).collectLatest {
                val result = receiveApiResult(it)
                if (result != null){
                    _selectedHouseWorkItem.value = result
                }
            }
        }
    }


    fun getRules() {
        viewModelScope.launch {
            mainRepository.getRules()
                .collectLatest {
                    val result = receiveApiResult(it)
                    if (result != null) {
                        _rule.value = when {
                            result.ruleResponseDtos.isNotEmpty() -> result.ruleResponseDtos.random().ruleName
                            else -> ""
                        }
                    }
                }
        }
    }

    fun getGroupName() {
        viewModelScope.launch {
            userRepository.getTeam().collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    val groupSize: Int = result.members.size
                    _groupName.value = "${result.teamName} $groupSize"

                    val myAssignee = result.members.find { it.memberId == PrefsManager.memberId }!!
                    val assignees = listOf(myAssignee) + result.members

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