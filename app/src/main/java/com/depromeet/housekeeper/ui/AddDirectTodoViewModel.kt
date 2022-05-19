package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.network.remote.repository.Repository
import com.depromeet.housekeeper.util.spaceNameMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddDirectTodoViewModel : ViewModel() {
    private val _curViewType: MutableStateFlow<ViewType> =
        MutableStateFlow(ViewType.ADD)
    val curViewType: StateFlow<ViewType>
        get() = _curViewType

    fun setViewType(viewType: ViewType) {
        _curViewType.value = viewType
    }


    private val _houseWorkId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val houseWorkId: StateFlow<Int>
        get() = _houseWorkId

    fun setHouseWorkId(id: Int) {
        _houseWorkId.value = id
    }

    private val _curDate: MutableStateFlow<String> =
        MutableStateFlow("")
    val curDate: StateFlow<String>
        get() = _curDate

    private val _curChoreName: MutableStateFlow<String> =
        MutableStateFlow("")
    val curChoreName: StateFlow<String?>
        get() = _curChoreName

    fun updateChoreName(name: String) {
        _chores.value[0].houseWorkName = name
    }

    private val _curTime: MutableStateFlow<String?> =
        MutableStateFlow(null)
    val curTime: StateFlow<String?>
        get() = _curTime

    fun updateTime(hour: Int, min: Int) {
        _curTime.value = "${String.format("%02d", hour)}:${String.format("%02d", min)}"
    }

    fun updateChoreTime(time: String?) {
        _chores.value[0].scheduledTime = time
    }

    private val _curSpace: MutableStateFlow<String> =
        MutableStateFlow(Chore.ETC_SPACE) // ETC
    val curSpace: StateFlow<String>
        get() = _curSpace

    fun bindingSpace():String {
        return spaceNameMapper(_curSpace.value)
    }

    // 직접 추가 or 수정은 chore 개수 1
    private val _chores: MutableStateFlow<ArrayList<Chore>> =
        MutableStateFlow(arrayListOf(Chore()))
    val chores: StateFlow<ArrayList<Chore>>
        get() = _chores

    fun initDirectChore() {
        // 기타 공간 직접 추가 집안일 정보 init
        _chores.value[0].scheduledDate = _curDate.value
        _chores.value[0].space = Chore.ETC_SPACE
    }

    fun initEditChore(chore: Chore) {
        // main에서 받아온 집안일 정보 init
        _chores.value[0].scheduledDate = chore.scheduledDate
        _chores.value[0].houseWorkName = chore.houseWorkName
        _chores.value[0].scheduledTime = chore.scheduledTime
        _chores.value[0].space = chore.space
        _chores.value[0].assignees = chore.assignees
    }


    private val calendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH))
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    private val _selectCalendar: MutableStateFlow<String> = MutableStateFlow("")
    val selectCalendar: StateFlow<String>
        get() = _selectCalendar

    fun updateCalendarView(year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val datePattern = "yyyy-MM-dd-EEE"
        _selectCalendar.value = SimpleDateFormat(datePattern, Locale.getDefault()).format(calendar.time)
    }

    // 집안일 직접 추가 api
    fun createHouseWorks() {
        viewModelScope.launch {
            Repository.createHouseWorks(Chores(_chores.value)).collect {
                Timber.d(it.houseWorks.toString())
            }
        }
    }

    fun editHouseWork() {
        viewModelScope.launch {
            Repository.editHouseWork(_houseWorkId.value, _chores.value[0]).collect {
                Timber.d(it.toString())
            }
        }
    }

    fun deleteHouseWork() {
        viewModelScope.launch {
            Repository.deleteHouseWork(_houseWorkId.value)
        }
    }

}
