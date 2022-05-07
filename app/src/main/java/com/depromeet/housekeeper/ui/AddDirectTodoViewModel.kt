package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.enums.ViewType
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class AddDirectTodoViewModel : ViewModel() {
    private val _curViewType: MutableStateFlow<ViewType> =
        MutableStateFlow(ViewType.ADD)
    val curViewType: StateFlow<ViewType>
        get() = _curViewType

    fun setViewType(viewType: ViewType) {
        _curViewType.value = viewType
    }

    private val _curDate: MutableStateFlow<String> =
        MutableStateFlow("2022년 * 4월 23일 토요일")
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

    // 직접 추가 or 수정은 chore 개수 1
    private val _chores: MutableStateFlow<ArrayList<Chore>> =
        MutableStateFlow(arrayListOf(Chore()))
    val chores: StateFlow<ArrayList<Chore>>
        get() = _chores

    fun initDirectChore() {
        // 기타 공간 직접 추가
        _chores.value[0].scheduledDate = _curDate.value
        _chores.value[0].space = Chore.ETC_SPACE
    }

    fun initEditChore() {
        // main에서 받아온 집안일 정보 init
    }

    // 집안일 직접 추가 api
    fun createHouseWorks() {
        viewModelScope.launch {
            Repository.createHouseWorks(Chores(_chores.value)).collect {
                Timber.d(it.houseWorks.toString())
            }
        }
    }
}
