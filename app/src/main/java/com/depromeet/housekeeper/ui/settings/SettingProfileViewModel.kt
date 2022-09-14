package com.depromeet.housekeeper.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.request.EditProfileModel
import com.depromeet.housekeeper.model.response.ProfileData
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _myData: MutableStateFlow<ProfileData?> = MutableStateFlow(null)
    val myData: StateFlow<ProfileData?>
        get() = _myData

    fun getProfile() {
        if (PrefsManager.getUserProfile().memberName == "" || PrefsManager.getUserProfile().profilePath == "") {
            getMe()
        } else {
            Timber.d("getProfile")
            _myData.value = PrefsManager.getUserProfile()
        }
    }

    fun setProfile(memberName: String, profilePath: String, statusMessage: String) {
        Timber.d("setProfile : $memberName, $profilePath, $statusMessage")
        PrefsManager.setUserProfile(ProfileData(memberName, profilePath, statusMessage))
    }

    /**
     * Network Communication
     */
    private fun getMe() {
        viewModelScope.launch {
            userRepository.getMe().runCatching {
                collect {
                    Timber.d("getMe : $it")
                    _myData.value = it
                }
            }.onFailure {
                Timber.e("getMe : $it")
            }
        }
    }

    fun updateMe(
        memberName: String,
        profilePath: String,
        statueMessage: String,
    ) {
        viewModelScope.launch {
            userRepository.updateMe(
                EditProfileModel(
                    memberName, profilePath, statueMessage
                )
            )
                .runCatching {
                    collect {
                        it.message
                    }
                }
        }
    }


}