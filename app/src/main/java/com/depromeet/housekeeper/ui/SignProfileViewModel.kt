package com.depromeet.housekeeper.ui

import android.view.View
import androidx.lifecycle.ViewModel
import com.depromeet.housekeeper.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignProfileViewModel : ViewModel() {
    private val _isSelectedView: MutableStateFlow<Int> = MutableStateFlow(0)
    val isSelectedView: StateFlow<Int>
        get() = _isSelectedView

    private val _isSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSelected: StateFlow<Boolean>
        get() = _isSelected

    private val _selectedImage: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedImage: StateFlow<Int>
        get() = _selectedImage

    fun onClick(view: View) {
        _isSelected.value = true
        when(view.id){
            R.id.sign_profile_select_imageview1->{
                _isSelectedView.value = 1
                _selectedImage.value = R.drawable.ic_profile1
                view.resources
            }
            R.id.sign_profile_select_imageview2->{
                //(_selectedImage.value = (view as? ImageView)?.resources)
                _isSelectedView.value = 2
                _selectedImage.value = R.drawable.ic_profile2
            }
            R.id.sign_profile_select_imageview3->{
                _isSelectedView.value = 3
                _selectedImage.value = R.drawable.ic_profile3
            }
            R.id.sign_profile_select_imageview4->{
                _isSelectedView.value = 4
                _selectedImage.value = R.drawable.ic_profile4
            }
        }


    }

}
