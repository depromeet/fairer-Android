package com.depromeet.housekeeper.ui.addHousework.selectSpace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.data.repository.Repository
import com.depromeet.housekeeper.model.request.ChoreList
import com.depromeet.housekeeper.model.DayOfWeek
import com.depromeet.housekeeper.util.dayMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class SelectSpaceViewModel : ViewModel() {
    init {
        getChoreList()
    }

    private val _chorePreset: MutableStateFlow<List<ChoreList>> = MutableStateFlow(listOf())
    val chorePreset: StateFlow<List<ChoreList>>
        get() = _chorePreset

    private val _choreList: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val choreList: StateFlow<List<String>>
        get() = _choreList

    private val _selectSpace: MutableStateFlow<String> = MutableStateFlow("")
    val selectSpace: StateFlow<String>
        get() = _selectSpace

    private val _chores: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val chores: StateFlow<List<String>>
        get() = _chores

    private val _isSelectedChore: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSelectedChore: StateFlow<Boolean>
        get() = _isSelectedChore

    private val _isSelectedSpace: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSelectedSpace: StateFlow<Boolean>
        get() = _isSelectedSpace

    fun clearChore() {
        _chores.value = emptyList()
    }

    fun setChoreList(space: String) {
        for (i in 0 until _chorePreset.value.size) {
            if (space == _chorePreset.value[i].space) {
                _choreList.value = _chorePreset.value[i].houseWorks
            }
        }
    }

    fun setIsSelectedChore(boolean: Boolean) {
        _isSelectedChore.value = boolean
    }

    fun setIsSelectedSpace(boolean: Boolean) {
        _isSelectedSpace.value = boolean
    }

    fun setSpace(space: String) {
        _selectSpace.value = space
    }

    fun getChoreCount(): Int {
        return _chores.value.size
    }

    fun updateChores(chores: String, isSelect: Boolean) {
        when {
            isSelect -> if (!_chores.value.contains(chores)) {
                _chores.value = _chores.value.plus(chores)
            }
            else -> if (_chores.value.contains(chores)) {
                _chores.value = _chores.value.minus(chores)
            }
        }
    }

    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError

    private fun getChoreList() {
        viewModelScope.launch {
            Repository.getHouseWorkList()
                .runCatching {
                    collect {
                        _chorePreset.value = it
                    }
                }.onFailure {
                    _networkError.value = true
                    Timber.d("networkerror")
                }

        }
    }

    private val calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    private val _selectCalendar: MutableStateFlow<DayOfWeek> =
        MutableStateFlow(DayOfWeek(date = ""))
    val selectCalendar: StateFlow<DayOfWeek>
        get() = _selectCalendar

    fun updateSelectDate(date: String) {
        _selectCalendar.value = DayOfWeek(date)
    }

    fun updateCalendarView(year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val datePattern = "yyyy-MM-dd-EEE"
        _selectCalendar.value =
            DayOfWeek(SimpleDateFormat(datePattern, Locale.getDefault()).format(calendar.time))
    }

    fun addCalendarView(selectDate: String) {
        _selectCalendar.value = DayOfWeek(date = selectDate)
    }

    fun bindingDate(): String {
        // yyyy-mm-dd-eee
        val str = _selectCalendar.value.date.split("-")
        val day = dayMapper(str[3])
        Timber.d("TAG ${str[3]}")
        return "${str[0]}년 ${str[1]}월 ${str[2]}일 $day"
    }

}