package com.depromeet.housekeeper.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.response.ProfileData
import com.depromeet.housekeeper.util.PrefsManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
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


    // google sign out
    fun signOut(context: Context) {
        logout()
        PrefsManager.setUserProfile(ProfileData("", "", ""))

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        mGoogleSignInClient.signOut()
            .addOnCompleteListener {
                if (it.isComplete)
                    Timber.d("google sign out")
            }

        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener {
                if (it.isComplete)
                    Timber.d("google account deleted")
            }
    }

    /**
     * Network Communication
     */
    private fun logout() {
        viewModelScope.launch {
            userRepository.logout()
                .runCatching {
                    collect {
                        Timber.d(it.toString())
                        PrefsManager.deleteTokens()
                        PrefsManager.deleteMemberInfo()
                    }
                }.onFailure {
                    _networkError.value = true
                }
        }
    }
}