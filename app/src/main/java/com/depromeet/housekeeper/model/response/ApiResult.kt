package com.depromeet.housekeeper.model.response

import com.depromeet.housekeeper.util.NETWORK_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T) : ApiResult<T>()
    data class Error<out T>(val code: Int, val message: String?) : ApiResult<T>()
    data class Exception(val e: Throwable) : ApiResult<Nothing>()
}

fun <T> safeFlow(apiFunc: suspend () -> T): Flow<ApiResult<T>> = flow {
    try {
        emit(ApiResult.Success(apiFunc.invoke()))
    }
    catch (e: HttpException) {
        if (e.code() == NETWORK_ERROR) {
            // todo 여기서 apiResult Loading으로 만들고 로딩중 화면 보여주게하려면 모든 네트워크 통신을 하나의 함수(baseRequest)에서 만들어서 해야할 듯
            Timber.e("auth ${e.message}")
        }
        else emit(ApiResult.Error(code = e.code(), message = e.stackTraceToString()))
    } catch (e: Exception) {
        emit(ApiResult.Exception(e))
    } catch (e: Throwable) {
        emit(ApiResult.Exception(e))
    }
}
