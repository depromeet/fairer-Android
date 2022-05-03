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
}