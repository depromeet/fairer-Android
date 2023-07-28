package com.depromeet.housekeeper.di

import android.annotation.SuppressLint
import com.depromeet.housekeeper.data.local.SessionManager
import com.depromeet.housekeeper.util.AUTHORIZATION
import com.depromeet.housekeeper.util.NETWORK_ERROR
import okhttp3.*
import timber.log.Timber
import java.net.HttpURLConnection
import javax.inject.Inject

class AuthInterceptor2 @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    @SuppressLint("TimberArgCount")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token: String? = sessionManager.getAccessToken() ?: sessionManager.getRefreshToken()

        try {
            Timber.d("token = $token")
            val response = chain.proceed(newRequestWithAccessToken(token!!, request))
            Timber.d("${response.code}")
            if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                val accessToken = sessionManager.getAccessToken()

                if (accessToken != null && accessToken != token) { // 앞선 api에서 이미 access token 변경됨
                    Timber.d("Access token is already changed")
                    return chain.proceed(newRequestWithAccessToken(accessToken, request))
                }

                val refreshToken = refreshToken()
                Timber.d("refreshToken = ${refreshToken}")
                if (refreshToken.isNullOrBlank()) { //refresh token 만료됨
                    Timber.d("Refresh token 없음")
                    //sessionManager.logout()
                    return response
                }

                val newResponse = chain.proceed(newRequestWithAccessToken(refreshToken, request))

                if (newResponse.code == HttpURLConnection.HTTP_OK) {
                    val newAccessToken: String? = newResponse.header(AUTHORIZATION, null)
                    Timber.d("new Access Token = ${newAccessToken}")
                    if (newAccessToken != null && newAccessToken != sessionManager.getAccessToken()) {
                        Timber.d("newAccessToken = ${newAccessToken}\nExistedAccessToken = ${accessToken}")
                        sessionManager.updateAccessToken(newAccessToken)
                    }
                }
                return newResponse

            }
            return response
        } catch (e: NullPointerException) {
            Timber.e(e.message)
//            synchronized(this) {
//                sessionManager.logout()
//            }
        } catch (e: Exception) {
            Timber.e(e.message)
//            synchronized(this) {
//                sessionManager.logout()
//            }
        }
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(NETWORK_ERROR)
            .message("")
            .body(ResponseBody.create(null, ""))
            .build()
    }

    private fun newRequestWithAccessToken(accessToken: String, request: Request): Request =
        request.newBuilder()
            .header(AUTHORIZATION, accessToken)
            .build()

    private fun refreshToken(): String? {
        synchronized(this) {
            return sessionManager.getRefreshToken()
            //todo refresh token 만료
//            refreshToken?.let {
//                return sessionManager.refreshToken(it)
//            }?: return null
        }
    }
}