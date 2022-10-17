package com.depromeet.housekeeper.util

import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.request.SocialType
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

class TokenErrorInterceptor() : Interceptor {
    @Inject
    lateinit var userRepository : UserRepository
    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenAddedRequest = chain.request()
        val response = chain.proceed(tokenAddedRequest)
        //todo 401에러 일때만 아래의 내용 처리
        if (response.code == 401) {
            Timber.d("401error")
            PrefsManager.deleteTokens()
            runBlocking{
                userRepository.getGoogleLogin(SocialType("GOOGLE")).runCatching {
                    collect{
                        PrefsManager.setTokens(it.accessToken, it.refreshToken)
                    }
                }
            }
        }
        try {
            chain.proceed(
                tokenAddedRequest.newBuilder()
                    .addHeader(
                        "Authorization",
                        PrefsManager.refreshToken.ifEmpty { PrefsManager.authCode })
                    .build()
            )
        } catch (e: Exception) {
            Response.Builder()
                .request(tokenAddedRequest)
                .protocol(Protocol.HTTP_1_1)
                .code(NETWORK_ERROR)
                .message(e.message ?: "")
                .body(ResponseBody.create(null, e.message ?: ""))
                .build()
        }
        return response

    }
}
