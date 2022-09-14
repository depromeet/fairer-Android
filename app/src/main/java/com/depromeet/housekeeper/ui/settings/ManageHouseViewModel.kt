package com.depromeet.housekeeper.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageHouseViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _response: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val response: StateFlow<Boolean>
        get() = _response

    fun leaveTeam() {
        viewModelScope.launch {
            userRepository.leaveTeam(
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