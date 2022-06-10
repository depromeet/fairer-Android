package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import com.depromeet.housekeeper.model.enums.SignViewType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignNameViewModel : ViewModel() {
    private val _inputName : MutableStateFlow<String> = MutableStateFlow("")
    val inputName : StateFlow<String>
    get() = _inputName

    private val _viewType : MutableStateFlow<SignViewType> = MutableStateFlow(SignViewType.UserName)
    val viewType : StateFlow<SignViewType>
    get() = _viewType

    fun setInputName(name:String){
        _inputName.value = name
    }

    fun setViewType(viewType : SignViewType){
        _viewType.value = viewType
    }


}