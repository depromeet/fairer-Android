package com.depromeet.housekeeper.repository

import com.depromeet.housekeeper.model.DataStoreManager
import com.depromeet.housekeeper.util.DataStoreKey
import kotlinx.coroutines.flow.Flow

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