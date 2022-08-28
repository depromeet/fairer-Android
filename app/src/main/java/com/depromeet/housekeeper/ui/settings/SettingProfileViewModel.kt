package com.depromeet.housekeeper.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.data.repository.Repository
import com.depromeet.housekeeper.model.EditProfileModel
import com.depromeet.housekeeper.model.ProfileData
import com.depromeet.housekeeper.util.PrefsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber


class SettingProfileViewModel : ViewModel() {

    init {
        Timber.d("create settingProfileViewModel")
    }

    private val _myData: MutableStateFlow<ProfileData?> = MutableStateFlow(null)
    val myData: StateFlow<ProfileData?>
        get() = _myData

    private fun getMe() {
        viewModelScope.launch {
            Repository.getMe().runCatching {
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
            Repository.updateMe(
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
}