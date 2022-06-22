package com.depromeet.housekeeper.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.SocialType
import com.depromeet.housekeeper.network.remote.model.LoginResponse
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _response: MutableStateFlow<LoginResponse?> = MutableStateFlow(null)
    val response: StateFlow<LoginResponse?>
        get() = _response

    private val _code: MutableStateFlow<String> = MutableStateFlow("")
    val code: StateFlow<String>
        get() = _code

    fun requestLogin(authCode: String) {
        viewModelScope.launch {
            Repository.getGoogleLogin(
                authCode, SocialType("GOOGLE")
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
}
