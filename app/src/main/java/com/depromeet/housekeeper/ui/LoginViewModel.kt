package com.depromeet.housekeeper.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.model.SocialType
import com.depromeet.housekeeper.model.Token
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

//    fun sendFCMTokenToServer(context: Context) {
//        val constraints = Constraints.Builder()
//            /* 네트워크 연결상태 & 배터리 부족에 대한 제약 조건 */
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .setRequiresCharging(true)
//            .build()
//
//        // 10, 20, 40초 늘려가며 retry
//        val workRequest = OneTimeWorkRequestBuilder<FCMWorker>()
//            .setConstraints(constraints)
//            .setBackoffCriteria(
//                BackoffPolicy.LINEAR,
//                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
//                TimeUnit.MILLISECONDS)
//            .build()
//
//        val workManager = WorkManager.getInstance(context)
//        workManager.enqueue(workRequest)
//    }

    fun sendToken() {
        viewModelScope.launch {
            Repository.saveToken(Token(token = PrefsManager.deviceToken))
                .runCatching {
                    collect {
                        Timber.d("set fcm response $this")
                    }
                }
                .onFailure {
                    Timber.d("$it")
                }
        }
    }
}
