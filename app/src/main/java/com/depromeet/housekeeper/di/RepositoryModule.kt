package com.depromeet.housekeeper.di

import com.depromeet.housekeeper.data.dataSource.RemoteDataSourceImpl
import com.depromeet.housekeeper.data.repository.MainRepository
import com.depromeet.housekeeper.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideUserRepository(remoteDataSourceImpl: RemoteDataSourceImpl): UserRepository {
        return UserRepository(remoteDataSourceImpl)
    }

    @Provides
    @ViewModelScoped
    fun provideMainRepository(remoteDataSourceImpl: RemoteDataSourceImpl): MainRepository {
        return MainRepository(remoteDataSourceImpl)
    }

}