package com.depromeet.housekeeper.ui.addHousework.selectSpace

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.MainRepository
import com.depromeet.housekeeper.model.DayOfWeek
import com.depromeet.housekeeper.model.request.ChoreList
import com.depromeet.housekeeper.ui.addHousework.selectSpace.adapter.SelectSpaceChoreAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SelectSpaceViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel() {
    init {
        getChoreList()
    }
    private val _choreAdapter : MutableStateFlow<SelectSpaceChoreAdapter?> = MutableStateFlow(null)
    val choreAdapter : StateFlow<SelectSpaceChoreAdapter?>
        get() = _choreAdapter

    private val _chorePreset: MutableStateFlow<List<ChoreList>> = MutableStateFlow(listOf())
    private val chorePreset: StateFlow<List<ChoreList>>
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

    private val calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    private val _selectCalendar: MutableStateFlow<DayOfWeek> =
        MutableStateFlow(DayOfWeek(date = ""))
    val selectCalendar: StateFlow<DayOfWeek>
        get() = _selectCalendar


    fun setChoreAdapter(adapter : SelectSpaceChoreAdapter) {
        _choreAdapter.value = adapter
    }

    fun clearChore() {
        _chores.value = emptyList()
    }

    fun setChoreList(space: String) {
        for (i in 0 until chorePreset.value.size) {
            if (space == chorePreset.value[i].space) {
                _choreList.value = chorePreset.value[i].houseWorks
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
        return "${str[0]}년 ${str[1]}월 ${str[2]}일"
    }


    /**
     * Network Communication
     */
    private fun getChoreList() {
        viewModelScope.launch {
            mainRepository.getHouseWorkList()
                .collectLatest {
                    val result = receiveApiResult(it)
                    if (result != null) {
                        _chorePreset.value = result
                    }
                }
        }
    }

}