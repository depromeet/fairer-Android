package com.depromeet.housekeeper.ui.settings

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.request.EditProfileModel
import com.depromeet.housekeeper.model.response.ProfileData
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    init {
        getMe()
    }

    private val _nameData:MutableStateFlow<String> = MutableStateFlow("")
    val nameData: StateFlow<String>
        get() = _nameData

    private val _massageData:MutableStateFlow<String> = MutableStateFlow("")
    val massageData: StateFlow<String>
        get() = _massageData

    private val _profileData:MutableStateFlow<String> = MutableStateFlow("")
    val profileData: StateFlow<String>
        get() = _profileData

    fun setNameData(name:String){
        _nameData.value = name
    }

    fun setMassageData(massage:String){
        _massageData.value = massage
    }

    fun setProfile(memberName: String, profilePath: String, statusMessage: String) {
        PrefsManager.setUserProfile(ProfileData(memberName, profilePath, statusMessage))
    }

    /**
     * Network Communication
     */
    fun getMe() {
        viewModelScope.launch {
            userRepository.getMe().collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    _nameData.value = result.memberName
                    _massageData.value = result.statusMessage
                    _profileData.value = result.profilePath
                }
            }
        }
    }

    fun updateMe(
        memberName: String,
        profilePath: String,
        statueMessage: String,
    ) {
        viewModelScope.launch {
            Timber.d("updateMe :  $memberName $profilePath $statueMessage")
            userRepository.updateMe(
                EditProfileModel(
                    memberName, profilePath, statueMessage
                )
            ).collectLatest {
                receiveApiResult(it)
            }
        }
    }

}