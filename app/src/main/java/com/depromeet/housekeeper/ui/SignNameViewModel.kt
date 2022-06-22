package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.BuildTeam
import com.depromeet.housekeeper.model.TeamUpdateResponse
import com.depromeet.housekeeper.model.enums.SignViewType
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignNameViewModel : ViewModel() {
    private val _inputText: MutableStateFlow<String> = MutableStateFlow("")
    val inputText: StateFlow<String>
        get() = _inputText

    private val _viewType: MutableStateFlow<SignViewType> = MutableStateFlow(SignViewType.UserName)
    val viewType: StateFlow<SignViewType>
        get() = _viewType

    private val _inviteCode: MutableStateFlow<String> = MutableStateFlow("")
    val inviteCode: StateFlow<String>
        get() = _inviteCode

    private val _hasTeam: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hasTeam: StateFlow<Boolean>
        get() = _hasTeam

    private val _response : MutableStateFlow<TeamUpdateResponse?> = MutableStateFlow(null)
    val response : StateFlow<TeamUpdateResponse?>
        get() = _response

    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError

    fun setInputText(name: String) {
        _inputText.value = name
    }

    fun setViewType(viewType: SignViewType) {
        _viewType.value = viewType
    }

    fun setHasTeam(hasTeam: Boolean) {
        _hasTeam.value = hasTeam
    }

    fun teamNameUpdate(teamName : String){
        viewModelScope.launch {
            Repository.updateTeam(BuildTeam(teamName)).runCatching {
                collect {
                    _response.value = it
                }
            }
                .onFailure {
                    _networkError.value = true
                }
        }
    }
}