package com.depromeet.housekeeper.model.response

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T) : ApiResult<T>()
    data class Error<out T>(val code: Int, val message: String?) : ApiResult<T>()
    data class Exception(val e: Throwable) : ApiResult<Nothing>()
}

fun <T> safeFlow(apiFunc: suspend () -> T): Flow<ApiResult<T>> = flow {
    try {
        emit(ApiResult.Success(apiFunc.invoke()))
    } catch (e: HttpException) {
        emit(ApiResult.Error(code = e.code(), message = e.message))
    } catch (e: Exception) {
        emit(ApiResult.Exception(e))
    } catch (e: Throwable) {
        emit(ApiResult.Exception(e))
    }
}
