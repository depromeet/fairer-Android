package com.depromeet.housekeeper.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.depromeet.housekeeper.BuildConfig
import com.depromeet.housekeeper.data.ApiService
import com.depromeet.housekeeper.data.dataSource.RemoteDataSourceImpl
import com.depromeet.housekeeper.data.utils.AuthAuthenticator
import com.depromeet.housekeeper.data.utils.AuthInterceptor
import com.depromeet.housekeeper.data.utils.RemoteConfigWrapper
import com.depromeet.housekeeper.data.utils.TokenManager
import com.depromeet.housekeeper.util.TOKEN_PREFERENCE_STORE
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val DEBUG_URL =
        "http://fairer-dev-env.eba-yzy7enxi.ap-northeast-2.elasticbeanstalk.com"
    private lateinit var RELEASE_URL: String
    private val BASE_URL: String by lazy {
        if (BuildConfig.DEBUG) DEBUG_URL else RELEASE_URL
    }

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        val dataStore = PreferenceDataStoreFactory.create(produceFile = {
            context.preferencesDataStoreFile(
                TOKEN_PREFERENCE_STORE
            )
        })
        return TokenManager(dataStore)
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager = tokenManager)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator =
        AuthAuthenticator(tokenManager = tokenManager)

    @Singleton
    @Provides
    fun provideOkHttpBuilder(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator
    ): OkHttpClient.Builder {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addNetworkInterceptor(httpLoggingInterceptor)
                .authenticator(authAuthenticator)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
        } else {
            OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .authenticator(authAuthenticator)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
        }

    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClientBuilder: OkHttpClient.Builder,
        remoteConfigWrapper: RemoteConfigWrapper
    ): Retrofit {
        RELEASE_URL = remoteConfigWrapper.fetchAndActivateConfig()
        Timber.d("RELEASE_URL = $RELEASE_URL")
        Timber.d("BASE_URL = $BASE_URL")

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
