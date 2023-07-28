package com.depromeet.housekeeper.model.response

import com.depromeet.housekeeper.util.NETWORK_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

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
        if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED || e.code() == NETWORK_ERROR) {
            // todo 여기서 apiResult Loading return하고 BaseViewModel에서 로딩중 화면 보여주게?
            Timber.e("auth ${e.message}")
        }
        else emit(ApiResult.Error(code = e.code(), message = e.stackTraceToString()))
    } catch (e: Exception) {
        emit(ApiResult.Exception(e))
    } catch (e: Throwable) {
        emit(ApiResult.Exception(e))
    }
}
