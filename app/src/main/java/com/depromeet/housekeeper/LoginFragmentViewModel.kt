package com.depromeet.housekeeper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.SocialType
import com.depromeet.housekeeper.network.remote.model.LoginResponse
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginFragmentViewModel : ViewModel() {

    private val SocialType : SocialType = SocialType("GOOGLE")

    private val _AuthCode : MutableStateFlow<String> = MutableStateFlow("")
    val AuthCode : StateFlow<String>
    get() = _AuthCode

    private val _Response : MutableStateFlow<LoginResponse?> = MutableStateFlow(null)
    val Response : StateFlow<LoginResponse?>
        get()=_Response

    fun getAuthcode(authcode : String){
        _AuthCode.value = authcode
    }

    fun getLoginResponse(){
        viewModelScope.launch {
            Repository.getGoogleLogin(_AuthCode.value,SocialType).collect {
                _Response.value = it
            }
        }
    }
}