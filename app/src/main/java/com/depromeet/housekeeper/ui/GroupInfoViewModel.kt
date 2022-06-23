package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GroupInfoViewModel : ViewModel() {
    private val _groupName : MutableStateFlow<String> = MutableStateFlow("즐거운 우리집")
    val groupName : StateFlow<String>
    get() = _groupName
}