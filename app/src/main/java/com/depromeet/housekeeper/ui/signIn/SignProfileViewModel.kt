package com.depromeet.housekeeper.ui.signIn

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.request.UpdateMember
import com.depromeet.housekeeper.model.response.UpdateMemberResponse
import com.depromeet.housekeeper.model.enums.ProfileViewType
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    init {
        setProfileImageList()
    }

    private val _viewType: MutableStateFlow<ProfileViewType> =
        MutableStateFlow(ProfileViewType.Sign)
    val viewType: StateFlow<ProfileViewType>
        get() = _viewType

    private val _memberName: MutableStateFlow<String> = MutableStateFlow("")
    val memberName: StateFlow<String>
        get() = _memberName

    private val _isSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSelected: StateFlow<Boolean>
        get() = _isSelected

    private val _selectedImage: MutableStateFlow<String> = MutableStateFlow("")
    val selectedImage: StateFlow<String>
        get() = _selectedImage

    private val _profileImageList: MutableStateFlow<MutableList<ProfileState>> =
        MutableStateFlow(arrayListOf())
    val profileImageList: StateFlow<List<ProfileState>>
        get() = _profileImageList

    private val _updateMemberResponse: MutableStateFlow<UpdateMemberResponse?> =
        MutableStateFlow(null)
    val updateMemberResponse: StateFlow<UpdateMemberResponse?>
        get() = _updateMemberResponse

    fun setViewType(viewType: ProfileViewType) {
        _viewType.value = viewType
    }

    fun setSelectedImage(imgUrl: String) {
        _selectedImage.value = imgUrl
        _isSelected.value = true
    }

    fun setMemberName(memberName: String) {
        _memberName.value = memberName
    }

    /**
     * Network Communication
     */
    private fun setProfileImageList() {
        val profileImages = mutableListOf<ProfileState>()
        viewModelScope.launch {
            userRepository.getProfileImages().collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    Timber.d("list get ${result.bigImageList.size}")
                    for (i in 0 until result.bigImageList.size) {
                        profileImages.add(ProfileState(result.bigImageList[i], false))
                    }
                    _profileImageList.value = profileImages
                    Timber.d("${_profileImageList.value}")
                }
            }
        }
    }

    fun requestUpdateMember() {
        PrefsManager.setUserProfilePath(selectedImage.value)
        viewModelScope.launch {
            userRepository.updateMember(
                UpdateMember(memberName.value, selectedImage.value)
            ).collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    _updateMemberResponse.value = result
                }
            }
        }
    }


    data class ProfileState(
        val url: String,
        var state: Boolean = false
    )
}
