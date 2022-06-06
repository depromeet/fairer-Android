package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingViewModel : ViewModel() {
    private val _version: MutableStateFlow<String> =
        MutableStateFlow("")
    val version: StateFlow<String>
        get() = _version

    fun setVersion(version: String) {
        _version.value = version
    }

    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError

    fun logout() {
        viewModelScope.launch {
            Repository.logout(PrefsManager.refreshToken)
                .runCatching {
                    collect {
                        Timber.d(it.toString())
                        PrefsManager.deleteTokens()
                    }
                }.onFailure {
                    _networkError.value = true
                }
        }

    }
}