package com.depromeet.housekeeper.di

import com.depromeet.housekeeper.data.local.SessionManager
import com.depromeet.housekeeper.util.AUTHORIZATION
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = sessionManager.getAccessToken()

        val response = chain.proceed(newRequestWithAccessToken(accessToken, request))

        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED){
            val newAccessToken = sessionManager.getAccessToken()
            if (newAccessToken != accessToken){ // 앞선 api에서 이미 access token 변경됨
                return chain.proceed(newRequestWithAccessToken(newAccessToken, request))
            } else {
                val refreshToken = refreshToken()

                if (refreshToken.isNullOrBlank()){ //refresh token 만료됨
                    sessionManager.logout()
                    return response
                }
                return chain.proceed(newRequestWithAccessToken(refreshToken, request))
            }
        } else if (response.code == HttpURLConnection.HTTP_OK){
            val newAccessToken: String? = response.header(AUTHORIZATION, null)
            if (newAccessToken != null && newAccessToken != sessionManager.getAccessToken())
                sessionManager.updateAccessToken(newAccessToken)
        }

        return response
    }

    private fun newRequestWithAccessToken(accessToken: String?, request: Request): Request =
        request.newBuilder()
            .header(AUTHORIZATION, "$accessToken")
            .build()

    private fun refreshToken(): String? {
        synchronized(this) {
            val refreshToken: String? = sessionManager.getRefreshToken()
            refreshToken?.let {
                return sessionManager.refreshToken(it)
            }?: return null
        }
    }
}