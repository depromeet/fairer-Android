package com.depromeet.housekeeper.ui.signIn

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.response.LoginResponse
import com.depromeet.housekeeper.model.request.SocialType
import com.depromeet.housekeeper.model.request.Token
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _response: MutableStateFlow<LoginResponse?> = MutableStateFlow(null)
    val response: StateFlow<LoginResponse?>
        get() = _response

    private val _code: MutableStateFlow<String> = MutableStateFlow("")
    val code: StateFlow<String>
        get() = _code


    /**
     * Network Communication
     */

    fun requestLogin() {
        viewModelScope.launch {
            userRepository.getGoogleLogin(
                SocialType("GOOGLE")
            ).collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    _response.value = result
                    Timber.d("!@#${result.memberName.toString()}")
                }
            }
        }
    }

    fun saveToken() {
        viewModelScope.launch {
            userRepository.saveToken(Token(token = PrefsManager.deviceToken))
                .collectLatest {
                    val result = receiveApiResult(it)
                    if (result != null) {
                        Timber.d("set fcm token response $this")
                    }
                }
        }
    }

}
