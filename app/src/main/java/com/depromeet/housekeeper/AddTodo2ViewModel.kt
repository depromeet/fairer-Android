package com.depromeet.housekeeper

import androidx.lifecycle.ViewModel
import com.depromeet.housekeeper.model.Chore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.collections.ArrayList

class AddTodo2ViewModel: ViewModel(){
    private val _curDate: MutableStateFlow<String> =
        MutableStateFlow("2022년 * 4월 23일 토요일")
    val curDate: StateFlow<String>
        get() = _curDate

    private val _curSpace: MutableStateFlow<String> =
        MutableStateFlow("방")
    val curSpace: StateFlow<String>
        get() = _curSpace

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

    fun removePosition(position: Int) {
        _positions.value.remove(position)
    }

    fun getPosition(type: PositionType):Int {
        return when (type) {
            PositionType.CUR -> _positions.value[_positions.value.size - 1]
            PositionType.PRE -> _positions.value[_positions.value.size - 2]
        }
    }
}

enum class PositionType {
    CUR, PRE
}
