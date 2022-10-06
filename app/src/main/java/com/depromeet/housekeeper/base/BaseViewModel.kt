package com.depromeet.housekeeper.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel: ViewModel() {
    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError

    fun setNetworkError(value: Boolean) {
        _networkError.value = value
    }
}