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
            ).runCatching {
                collect {
                    _response.value = it
                    Timber.d("!@#${it.memberName.toString()}")
                }
            }.onFailure {
                Timber.e("$it")
            }
        }
    }

    fun saveToken() {
        viewModelScope.launch {
            userRepository.saveToken(Token(token = PrefsManager.deviceToken))
                .runCatching {
                    collect {
                        Timber.d("set fcm token response $this")
                    }
                }
                .onFailure {
                    Timber.d("set fcm token error $it")
                }
        }
    }

}
