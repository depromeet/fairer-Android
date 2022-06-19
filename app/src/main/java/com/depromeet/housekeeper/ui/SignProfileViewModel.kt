package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import com.depromeet.housekeeper.model.enums.ProfileViewType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignProfileViewModel : ViewModel() {
    private val _viewType : MutableStateFlow<ProfileViewType> = MutableStateFlow(ProfileViewType.Sign)
    val viewType : StateFlow<ProfileViewType>
        get() = _viewType

    private val _isSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSelected: StateFlow<Boolean>
        get() = _isSelected

    private val _selectedImage: MutableStateFlow<String> = MutableStateFlow("")
    val selectedImage: StateFlow<String>
        get() = _selectedImage

    fun setViewType(viewType : ProfileViewType){
        _viewType.value = viewType
    }

    fun setSelectedImage(imgUrl : String){
        _selectedImage.value = imgUrl
        _isSelected.value = true
    }

}
