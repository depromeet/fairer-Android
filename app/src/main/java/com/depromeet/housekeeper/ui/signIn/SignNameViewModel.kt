package com.depromeet.housekeeper.ui.signIn

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.enums.SignViewType
import com.depromeet.housekeeper.model.request.BuildTeam
import com.depromeet.housekeeper.model.request.JoinTeam
import com.depromeet.housekeeper.model.response.Groups
import com.depromeet.housekeeper.model.response.JoinTeamResponse
import com.depromeet.housekeeper.model.response.TeamUpdateResponse
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignNameViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
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

    private val _isDynamicLink: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDynamicLink: StateFlow<Boolean>
        get() = _isDynamicLink

    fun setDynamicLink(boolean: Boolean) {
        _isDynamicLink.value = boolean
    }

    //후에 response를 사용할 수 있으므로 남겨놓음
    private val _responseTeamUpdate: MutableStateFlow<TeamUpdateResponse?> = MutableStateFlow(null)
    val responseTeamUpdate: StateFlow<TeamUpdateResponse?>
        get() = _responseTeamUpdate

    private val _responseJoinTeam: MutableStateFlow<JoinTeamResponse?> = MutableStateFlow(null)
    val responseJoinTeam: StateFlow<JoinTeamResponse?>
        get() = _responseJoinTeam

    private val _responseMyTeam: MutableStateFlow<Groups?> = MutableStateFlow(null)
    val responseMyTeam: StateFlow<Groups?>
        get() = _responseMyTeam

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: StateFlow<String>
        get() = _errorMessage

    private var _isNextBtnClickable: Boolean = false
    val isNextBtnClickable get() = _isNextBtnClickable

    fun setMemberName() {
        PrefsManager.setUserName(inputText.value)
    }

    fun setIsNextBtnClickable(isClickable: Boolean) {
        _isNextBtnClickable = isClickable
    }

    fun setInputText(name: String) {
        _inputText.value = name
    }

    fun setViewType(viewType: SignViewType) {
        _viewType.value = viewType
    }

    fun setErrorMessage() {
        _errorMessage.value = ""
    }

    fun setHasTeam(hasTeam: Boolean) {
        _hasTeam.value = hasTeam
    }

    /**
     * Network Communication
     */
    fun teamNameUpdate(teamName: String) {
        viewModelScope.launch {
            userRepository.updateTeam(BuildTeam(teamName)).collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    _responseTeamUpdate.value = result
                }
            }
        }
    }

    fun joinTeam(inviteCode: String) {
        viewModelScope.launch {
            Timber.d("joinTeam: $inviteCode")

            userRepository.joinTeam(JoinTeam(inviteCode)).collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    _responseJoinTeam.value = result
                    PrefsManager.setHasTeam(hasTeam = true)
                }
            }
        }
    }
    fun getMyTeam(){
        viewModelScope.launch {
            userRepository.getTeam().collectLatest {
                val result = receiveApiResult(it)
                if(result != null){
                    _responseMyTeam.value = result
                }
            }
        }
    }

}