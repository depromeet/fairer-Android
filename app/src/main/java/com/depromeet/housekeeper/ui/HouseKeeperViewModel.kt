package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HouseKeeperViewModel : ViewModel() {
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _inviteCode: MutableStateFlow<String> = MutableStateFlow("")
    val inviteCode: StateFlow<String>
        get() = _inviteCode

    private var _isNetworkDisconnected: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isNetworkDisconnected get() = _isNetworkDisconnected

    init {
        viewModelScope.launch {
            delay(1000)
            _isLoading.value = false
        }
    }

    fun setIsNetworkDisconnected(value: Boolean) {
        _isNetworkDisconnected.value = value
    }

}