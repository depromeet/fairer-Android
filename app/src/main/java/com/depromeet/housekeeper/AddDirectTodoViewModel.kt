package com.depromeet.housekeeper

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddDirectTodoViewModel : ViewModel() {
    private val _curDate: MutableStateFlow<String> =
        MutableStateFlow("2022년 * 4월 23일 토요일")
    val curDate: StateFlow<String>
        get() = _curDate

    private val _curSpace: MutableStateFlow<String> =
        MutableStateFlow("기타 공간")
    val curSpace: StateFlow<String>
        get() = _curSpace

}