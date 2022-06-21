package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.enums.ProfileViewType
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class SignProfileViewModel : ViewModel() {
    init {
        setProfileImageList()
    }
    private val _viewType : MutableStateFlow<ProfileViewType> = MutableStateFlow(ProfileViewType.Sign)
    val viewType : StateFlow<ProfileViewType>
        get() = _viewType

    private val _isSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSelected: StateFlow<Boolean>
        get() = _isSelected

    private val _selectedImage: MutableStateFlow<String> = MutableStateFlow("")
    val selectedImage: StateFlow<String>
        get() = _selectedImage

    private val _profileImageList : MutableStateFlow<MutableList<ProfileState>> = MutableStateFlow(arrayListOf())
    val profileImageList : StateFlow<List<ProfileState>>
        get() = _profileImageList

    fun setViewType(viewType : ProfileViewType){
        _viewType.value = viewType
    }

    fun setSelectedImage(imgUrl : String){
        _selectedImage.value = imgUrl
        _isSelected.value = true
    }

    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError

    private fun setProfileImageList(){
        var profileImages = mutableListOf<ProfileState>()
        viewModelScope.launch {
            Repository.getProfileImages().runCatching {
                collect {
                    Timber.d("list get ${it.bigImageList.size}")
                        for(i in 0 until it.bigImageList.size)
                        {
                            profileImages.add(ProfileState(it.bigImageList[i],false))
                        }
                    _profileImageList.value = profileImages
                    Timber.d("${_profileImageList.value}")
                }
            }
                .onFailure {
                    _networkError.value = true
                }
        }
    }
    data class ProfileState(
        val url : String,
        var state : Boolean = false
    )
}
