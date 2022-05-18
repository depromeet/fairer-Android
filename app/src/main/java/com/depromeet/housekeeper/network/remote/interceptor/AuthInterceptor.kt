package com.depromeet.housekeeper.network.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class AuthInterceptor : Interceptor {
  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
    proceed(
      request().newBuilder()
        .addHeader("AUTHORIZATION", CoroutineScopDataStoreRepository().getAccessToken("null").toString())
        .build()
    )
  }
}