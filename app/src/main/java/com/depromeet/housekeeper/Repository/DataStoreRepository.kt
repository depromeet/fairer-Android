package com.depromeet.housekeeper.Repository

import com.depromeet.housekeeper.model.DataStoreManager
import com.depromeet.housekeeper.util.DataStoreKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataStoreRepository constructor(private val dataStoreManager: DataStoreManager) {

    suspend fun saveAccessToken(accessToken: String){
        dataStoreManager.edit(DataStoreKey.AccessTokenKey,accessToken)
    }

    suspend fun getAccessToken(defaultValue: String): Flow<String>{
        return dataStoreManager.get(DataStoreKey.AccessTokenKey,defaultValue)
    }


    suspend fun saveRefreshToken(RefreshToken: String){
        dataStoreManager.edit(DataStoreKey.RefreshTokenKey,RefreshToken)
    }

    suspend fun getRefreshToken(defaultValue: String): Flow<String> {
        return dataStoreManager.get(DataStoreKey.RefreshTokenKey,defaultValue)
    }

}