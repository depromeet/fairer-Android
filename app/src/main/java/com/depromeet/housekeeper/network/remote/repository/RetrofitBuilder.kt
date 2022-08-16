package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.network.remote.api.ApiService
import com.depromeet.housekeeper.util.NETWORK_ERROR
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
  private const val BASE_URL = "http://ec2-3-39-60-64.ap-northeast-2.compute.amazonaws.com:8080"

  private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  private val okHttpBuilder = OkHttpClient.Builder()
    .addInterceptor(NetworkInterceptor())
    .addNetworkInterceptor(httpLoggingInterceptor)
    .connectTimeout(5, TimeUnit.SECONDS)
    .readTimeout(5, TimeUnit.SECONDS)
    .writeTimeout(5, TimeUnit.SECONDS)

  private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpBuilder.build())
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

  val apiService: ApiService = retrofit.create(ApiService::class.java)

  private class NetworkInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
      val request = chain.request()
      return try {
        chain.proceed(
          request.newBuilder()
            .addHeader("Authorization", PrefsManager.refreshToken.ifEmpty { PrefsManager.authCode })
            .build()
        )
      } catch (e: Exception) {
        Response.Builder()
          .request(request)
          .protocol(Protocol.HTTP_1_1)
          .code(NETWORK_ERROR)
          .message(e.message ?: "")
          .body(ResponseBody.create(null, e.message ?: ""))
          .build()
      }
    }
  }

}
