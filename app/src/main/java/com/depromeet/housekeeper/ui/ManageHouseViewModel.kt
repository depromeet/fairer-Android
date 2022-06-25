package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ManageHouseViewModel : ViewModel() {
    private val _response: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val response: StateFlow<Boolean>
        get() = _response

    fun leaveTeam() {
        viewModelScope.launch {
            Repository.leaveTeam(
            ).runCatching {
                collect {
                    _response.value = true
                    PrefsManager.setHasTeam(false)
                }
            }
                .onFailure {
                }
        }

    }
}