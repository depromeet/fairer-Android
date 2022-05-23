package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.network.remote.repository.Repository
import com.depromeet.housekeeper.util.dayMapper
import com.depromeet.housekeeper.util.spaceNameMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class AddTodo2ViewModel: ViewModel(){
    private val _curDate: MutableStateFlow<String> =
    MutableStateFlow("")
  val curDate: StateFlow<String>
    get() = _curDate

    private val datePattern = "yyyy-MM-dd"

    fun setDate(date: String) {
      val lastIndex = date.indexOfLast { it == '-' }
      val requestDate = date.dropLast(date.length - lastIndex)
      _curDate.value = requestDate
    }

    fun getDate(): String {
        val year = LocalDate.parse(_curDate.value).year.toString()
        val month = LocalDate.parse(_curDate.value).month.toString()
        val day = LocalDate.parse(_curDate.value).dayOfMonth.toString()
        return "${year}년 ${month}월 ${day}일 "
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

    fun initChores(space:String, choreName: List<String>) {
        val temp = arrayListOf<Chore>()
        choreName.map{ name ->
            val chore = Chore()
            chore.scheduledDate = curDate.value
            chore.space = space.uppercase()
            chore.houseWorkName = name
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

    fun getChores() : ArrayList<Chore>{
        return _chores.value
    }

    fun createHouseWorks() {
        viewModelScope.launch {
            Repository.createHouseWorks(Chores(_chores.value)).collect {
                Timber.d(it.houseWorks.toString())
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
        return "${str[0]}년 ${str[1]}월 ${str[2]}일 $day"
    }
}

enum class PositionType {
    CUR, PRE
}
