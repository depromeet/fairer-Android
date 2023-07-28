package com.depromeet.housekeeper.data.utils

import com.depromeet.housekeeper.util.AUTHORIZATION
import com.depromeet.housekeeper.util.PrefsManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.HTTP_OK
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getAccessToken().first() ?: PrefsManager.authCode!!
        }
        val request = chain.request().newBuilder().header(AUTHORIZATION, token).build()
        val response = chain.proceed(request)

        if (response.code == HTTP_OK) {
            val newAccessToken: String = response.header(AUTHORIZATION, null) ?: return response
            Timber.d("new Access Token = ${newAccessToken}")

            runBlocking {
                val existedAccessToken = tokenManager.getAccessToken().first()
                if (existedAccessToken != newAccessToken) {
                    tokenManager.saveAccessToken(newAccessToken)
                    Timber.d("newAccessToken = ${newAccessToken}\nExistedAccessToken = ${existedAccessToken}")
                }
            }

        }
        return response
    }

}