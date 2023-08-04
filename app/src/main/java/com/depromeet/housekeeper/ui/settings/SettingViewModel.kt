package com.depromeet.housekeeper.ui.settings

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.data.utils.TokenManager
import com.depromeet.housekeeper.model.response.ProfileData
import com.depromeet.housekeeper.util.PrefsManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : BaseViewModel() {
    private val _version: MutableStateFlow<String> =
        MutableStateFlow("")
    val version: StateFlow<String>
        get() = _version

    fun setVersion(version: String) {
        _version.value = version
    }

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
            withContext(Dispatchers.IO){
                tokenManager.deleteTokens()
            }
            userRepository.logout().collectLatest {
                receiveApiResult(it) ?: return@collectLatest
                Timber.d(it.toString())
            }
        }
    }
}