package com.depromeet.housekeeper.data.utils


import com.depromeet.housekeeper.util.AUTHORIZATION
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val refreshToken = runBlocking {
            tokenManager.getRefreshToken().first()
        }
        Timber.d("refreshToken = ${refreshToken}")
        //runBlocking { tokenManager.deleteRefreshToken() }
        if (refreshToken != null) return newRequestWithToken(refreshToken, response.request)
        return response.request
    }

    private fun newRequestWithToken(token: String, request: Request): Request =
        request.newBuilder()
            .header(AUTHORIZATION, token)
            .build()
}