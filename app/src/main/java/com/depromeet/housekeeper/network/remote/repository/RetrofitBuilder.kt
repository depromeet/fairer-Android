package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.network.remote.api.ApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
  private const val BASE_URL = "http://ec2-13-125-232-180.ap-northeast-2.compute.amazonaws.com:8080"

  private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  private val okHttpBuilder = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor())
    .addNetworkInterceptor(httpLoggingInterceptor)
    .connectTimeout(1, TimeUnit.SECONDS)
    .readTimeout(1, TimeUnit.SECONDS)
    .writeTimeout(1, TimeUnit.SECONDS)

  private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpBuilder.build())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

  val apiService: ApiService = retrofit.create(ApiService::class.java)

  private class AuthInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
      proceed(
        request().newBuilder()
          .addHeader("AUTHORIZATION", PrefsManager.accessToken)
          .build()
      )
    }
  }
}
