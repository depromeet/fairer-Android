package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingViewModel : ViewModel() {
    private val _version: MutableStateFlow<String> =
        MutableStateFlow("")
    val version: StateFlow<String>
        get() = _version

    fun setVersion(version: String) {
        _version.value = version
    }
}