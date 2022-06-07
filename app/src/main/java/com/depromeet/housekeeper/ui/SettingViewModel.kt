package com.depromeet.housekeeper.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.network.remote.repository.Repository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber


class SettingViewModel : ViewModel() {
    private val _version: MutableStateFlow<String> =
        MutableStateFlow("")
    val version: StateFlow<String>
        get() = _version

    fun setVersion(version: String) {
        _version.value = version
    }

    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError

    fun logout() {
        viewModelScope.launch {
            Repository.logout(PrefsManager.refreshToken)
                .runCatching {
                    collect {
                        Timber.d(it.toString())
                        PrefsManager.deleteTokens()
                    }
                }.onFailure {
                    _networkError.value = true
                }
        }
    }

    // google sign out
    fun signOut(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        mGoogleSignInClient.signOut()
            .addOnCompleteListener {
                if(it.isComplete)
                    Timber.d("google sign out")
            }

        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener {
                if(it.isComplete)
                    Timber.d("google account deleted")
            }
    }
}