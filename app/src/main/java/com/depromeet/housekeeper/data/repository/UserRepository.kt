package com.depromeet.housekeeper.data.repository


import com.depromeet.housekeeper.data.dataSource.RemoteDataSourceImpl
import com.depromeet.housekeeper.model.request.SocialType
import com.depromeet.housekeeper.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl
) {

    suspend fun getGoogleLogin(socialType: SocialType): Flow<LoginResponse> =
        remoteDataSource.getGoogleLogin(socialType)

    suspend fun logout(): Flow<Unit> = remoteDataSource.logout()


}