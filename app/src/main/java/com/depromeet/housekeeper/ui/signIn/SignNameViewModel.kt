package com.depromeet.housekeeper.ui.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.data.repository.Repository
import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.model.enums.SignViewType
import com.depromeet.housekeeper.util.PrefsManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

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

    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: StateFlow<String>
        get() = _errorMessage

    private var _isNextBtnClickable : Boolean = false
    val isNextBtnClickable get() = _isNextBtnClickable

    fun setMemberName(){
        PrefsManager.setUserName(inputText.value)
    }

    fun setIsNextBtnClickable(isClickable : Boolean) {
        _isNextBtnClickable = isClickable
    }

    fun setInputText(name: String) {
        _inputText.value = name
    }

    fun setViewType(viewType: SignViewType) {
        _viewType.value = viewType
    }
    fun setErrorMessage(){
        _errorMessage.value = ""
    }

    fun setHasTeam(hasTeam: Boolean) {
        _hasTeam.value = hasTeam
    }

    fun teamNameUpdate(teamName: String) {
        viewModelScope.launch {
            Repository.updateTeam(BuildTeam(teamName)).runCatching {
                collect {
                    _responseTeamUpdate.value = it
                }
            }
                .onFailure {
                    _networkError.value = true
                }
        }
    }

    fun joinTeam(inviteCode: String) {
        viewModelScope.launch {
            Timber.d("joinTeam: $inviteCode")

            Repository.joinTeam(JoinTeam(inviteCode)).runCatching {
                collect {
                    _responseJoinTeam.value = it
                    PrefsManager.setHasTeam(hasTeam = true)
                }
            }.onFailure {
                when (it) {
                    is HttpException -> {
                        val errorBody = it.response()?.errorBody()
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(errorBody?.charStream(), type)
                        _errorMessage.value = errorResponse.errorMessage
                    }
                    else -> {
                        Timber.e("onFailure: $it")
                    }
                }
            }
        }
    }
}