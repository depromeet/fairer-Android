package com.depromeet.housekeeper.util

import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.request.SocialType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber

class TokenErrorInterceptor() : Interceptor {
    private lateinit var userRepository : UserRepository

    override fun intercept(chain: Interceptor.Chain): Response {

        CoroutineScope(Dispatchers.IO).launch {
            userRepository.getGoogleLogin(SocialType("GOOGLE")).runCatching {
                collect { response ->
                    PrefsManager.setTokens(response.accessToken, response.refreshToken)
                }
            }.onFailure {
                Timber.e("$it")
            }
        }

        val tokenAddedRequest = chain.request()
        try{
            tokenAddedRequest.newBuilder()
                .addHeader(
                    "authorization",
                    PrefsManager.refreshToken.ifEmpty { PrefsManager.authCode })
                .build()
        } catch (e: Exception) {
            Response.Builder()
                .request(tokenAddedRequest)
                .protocol(Protocol.HTTP_1_1)
                .code(NETWORK_ERROR)
                .message(e.message ?: "")
                .body(ResponseBody.create(null, e.message ?: ""))
                .build()
        }
            return chain.proceed(tokenAddedRequest)
    }

}
