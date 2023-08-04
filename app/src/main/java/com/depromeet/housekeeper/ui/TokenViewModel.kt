package com.depromeet.housekeeper.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.data.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val tokenManager: TokenManager
): ViewModel() {
    val refreshToken = MutableLiveData<String?>()

    init {
        viewModelScope.launch(Dispatchers.IO){
            tokenManager.getRefreshToken().collect{
                withContext(Dispatchers.Main) { refreshToken.value = it}
            }
        }
    }


}