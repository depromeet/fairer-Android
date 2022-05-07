package com.depromeet.housekeeper.network.remote.repository

import com.depromeet.housekeeper.network.remote.api.ApiService
import com.depromeet.housekeeper.network.remote.api.ChoreListApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object RetrofitBuilder {
  private const val BASE_URL = "http://ec2-13-125-232-180.ap-northeast-2.compute.amazonaws.com:8080"

  private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  private val okHttpBuilder = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)

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
  val ChoreList : ChoreListApi = retrofit.create(ChoreListApi::class.java)
}
