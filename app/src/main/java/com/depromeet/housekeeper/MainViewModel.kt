package com.depromeet.housekeeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel : ViewModel(){
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading : StateFlow<Boolean>
    get() = _isLoading

    private val _inviteCode : MutableStateFlow<String> = MutableStateFlow("")
    val inviteCode : StateFlow<String>
    get() = _inviteCode

    init {
        viewModelScope.launch{
            delay(1000)
            _isLoading.value = false
        }
    }

    fun setInviteCode(code : String){
        _inviteCode.value = code
    }
}