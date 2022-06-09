package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignNameViewModel : ViewModel() {
    private val _inputName : MutableStateFlow<String> = MutableStateFlow("")
    val inputName : StateFlow<String>
    get() = _inputName



    fun setInputName(name:String){
        _inputName.value = name
    }

}