package com.depromeet.housekeeper.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.model.SocialType
import com.depromeet.housekeeper.model.Token
import com.depromeet.housekeeper.network.remote.model.LoginResponse
import com.depromeet.housekeeper.network.remote.repository.Repository
import com.depromeet.housekeeper.service.FCMWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _response: MutableStateFlow<LoginResponse?> = MutableStateFlow(null)
    val response: StateFlow<LoginResponse?>
        get() = _response

    private val _code: MutableStateFlow<String> = MutableStateFlow("")
    val code: StateFlow<String>
        get() = _code

    fun requestLogin() {
        viewModelScope.launch {
            Repository.getGoogleLogin(
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
            Repository.saveToken(Token(token = PrefsManager.deviceToken))
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
