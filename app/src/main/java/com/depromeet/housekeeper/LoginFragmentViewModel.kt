package com.depromeet.housekeeper

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.repository.DataStoreRepository
import com.depromeet.housekeeper.model.DataStoreManager
import com.depromeet.housekeeper.model.SocialType
import com.depromeet.housekeeper.network.remote.model.LoginResponse
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class LoginFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val context : Context = application
    private val dataStoreRepository : DataStoreRepository = DataStoreRepository(DataStoreManager(context))
    private val SocialType : SocialType = SocialType("GOOGLE")

    private val _AuthCode : MutableStateFlow<String> = MutableStateFlow("")
    val AuthCode : StateFlow<String>
    get() = _AuthCode

    private val _AccessToken : MutableStateFlow<String> = MutableStateFlow("")
    val AccessToken : StateFlow<String>
        get() = _AccessToken

    private val _RefreshToken : MutableStateFlow<String> = MutableStateFlow("")
    val RefreshToken : StateFlow<String>
        get() = _RefreshToken

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
                _AccessToken.value = _Response.value!!.accessToken
                _RefreshToken.value = _Response.value!!.refreshToken
                Timber.d("accesstoken:${_AccessToken.value}, refreshtoken:${_RefreshToken.value}")
                saveTokens()
            }
        }
    }

    private fun saveTokens(){
        viewModelScope.launch {
            _Response.value?.let { dataStoreRepository.saveAccessToken(it.accessToken) }
            _Response.value?.let { dataStoreRepository.saveRefreshToken(it.refreshToken) }
        }
    }


    fun getTokens(){
            runBlocking {
                _AccessToken.value = dataStoreRepository.getAccessToken("null").first()
                _RefreshToken.value = dataStoreRepository.getRefreshToken("null").first()
            }
        }
}