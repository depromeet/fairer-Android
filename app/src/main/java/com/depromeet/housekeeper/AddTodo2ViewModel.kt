package com.depromeet.housekeeper

import android.icu.util.LocaleData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.model.DayOfWeek
import com.depromeet.housekeeper.network.remote.repository.Repository
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
        MutableStateFlow("2022-04-23")
    val curDate: StateFlow<String>
        get() = _curDate

    private val datePattern = "yyyy-MM-dd"
    fun setDate() {
        val format = SimpleDateFormat(datePattern, Locale.getDefault())
        _curDate.value = format.format(Calendar.getInstance().time)
    }

    fun getDate(): String {
        val year = LocalDate.parse(_curDate.value).year.toString()
        val month = LocalDate.parse(_curDate.value).month.toString()
        val day = LocalDate.parse(_curDate.value).dayOfMonth.toString()
        return "${year}년 * ${month}월 ${day}일 "
    }

    private val _curSpace: MutableStateFlow<String> =
        MutableStateFlow("방")
    val curSpace: StateFlow<String>
        get() = _curSpace

    fun updateSpace(space: String) {
        _curSpace.value = space
    }

    fun getSpace(): String {
        return _curSpace.value
    }

    private val _curTime: MutableStateFlow<String> =
        MutableStateFlow(Chore.DEFAULT_TIME)
    val curTime: StateFlow<String>
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
            chore.scheduleDate = _curDate.value
            chore.space = space.uppercase()
            chore.houseWorkName = name
            temp.add(chore)
        }
        _chores.value.addAll(temp)
    }

    fun updateChore(time: String, position: Int) {
        _chores.value[position].scheduleTime = time
    }

    fun getChore(position: Int): Chore {
        return _chores.value[position]
    }

    fun getChores() : ArrayList<Chore>{
        return _chores.value
    }

    fun createHouseWorks() {
        viewModelScope.launch {
            Repository.createHouseWorks(_chores.value).collect {
                Timber.d(it.toString())
            }
        }
    }
}

enum class PositionType {
    CUR, PRE
}
