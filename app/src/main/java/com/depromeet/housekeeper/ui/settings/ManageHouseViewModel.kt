package com.depromeet.housekeeper.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.data.repository.Repository
import com.depromeet.housekeeper.util.PrefsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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