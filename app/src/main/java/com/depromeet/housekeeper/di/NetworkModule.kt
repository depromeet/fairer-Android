package com.depromeet.housekeeper.di

import com.depromeet.housekeeper.BuildConfig
import com.depromeet.housekeeper.data.ApiService
import com.depromeet.housekeeper.data.dataSource.RemoteDataSourceImpl
import com.depromeet.housekeeper.util.NETWORK_ERROR
import com.depromeet.housekeeper.util.PrefsManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val DEBUG_URL =
        "http://fairer-dev-env.eba-yzy7enxi.ap-northeast-2.elasticbeanstalk.com/"

    private val RELEASE_URL =
        "https://fairer-env.eba-synb99hd.ap-northeast-2.elasticbeanstalk.com"
    private val BASE_URL: String = if(BuildConfig.DEBUG) DEBUG_URL else RELEASE_URL

    private val networkInterceptor: Interceptor = Interceptor { chain ->
        val request = chain.request()
        try {
            chain.proceed(
                request.newBuilder()
                    .addHeader(
                        "Authorization",
                        PrefsManager.refreshToken.ifEmpty { PrefsManager.authCode })
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

    @Singleton
    @Provides
    fun provideOkHttpBuilder(): OkHttpClient.Builder {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return if(BuildConfig.DEBUG){
            OkHttpClient.Builder()
                .addInterceptor(networkInterceptor)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
        } else{
            OkHttpClient.Builder()
                .addInterceptor(networkInterceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
        }

    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClientBuilder: OkHttpClient.Builder): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSourceImpl {
        return RemoteDataSourceImpl(apiService, Dispatchers.IO)
    }
}
