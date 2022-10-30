package com.depromeet.housekeeper.base

import androidx.lifecycle.ViewModel
import com.depromeet.housekeeper.model.response.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

open class BaseViewModel : ViewModel() {
    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError

    fun setNetworkError(value: Boolean) {
        _networkError.value = value
    }

    fun <T : Any> receiveApiResult(result: ApiResult<T>): T? {
        when (result) {
            is ApiResult.Success -> {
                return result.value
            }
            is ApiResult.Error -> {
                setNetworkError(true)
                Timber.e("ApiResult Error : code = ${result.code}, msg = ${result.message}")
            }
            is ApiResult.Exception -> {
                //setNetworkError(true)
                Timber.e("ApiResult Exception : ${result.e}")
            }
        }
        return null
    }
}