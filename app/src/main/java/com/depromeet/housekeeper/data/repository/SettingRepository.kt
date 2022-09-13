package com.depromeet.housekeeper.data.repository


import com.depromeet.housekeeper.data.dataSource.RemoteDataSourceImpl
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl
) {

}