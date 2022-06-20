package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import com.depromeet.housekeeper.model.enums.SignViewType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignNameViewModel : ViewModel() {
    private val _inputText : MutableStateFlow<String> = MutableStateFlow("")
    val inputText : StateFlow<String>
    get() = _inputText

    private val _viewType : MutableStateFlow<SignViewType> = MutableStateFlow(SignViewType.UserName)
    val viewType : StateFlow<SignViewType>
    get() = _viewType

    private val _inviteCode : MutableStateFlow<String> = MutableStateFlow("")
    val inviteCode : StateFlow<String>
        get() = _inviteCode

    private val _hasTeam : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hasTeam : StateFlow<Boolean>
        get() = _hasTeam

    fun setInputText(name:String){
        _inputText.value = name
    }

    fun setViewType(viewType : SignViewType){
        _viewType.value = viewType
    }
    fun setHasTeam(hasTeam:Boolean){
        _hasTeam.value = hasTeam
    }
}