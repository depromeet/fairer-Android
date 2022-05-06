package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.ChoreList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChoreListApi {
    @GET("/api/houseworks/{space}")
    suspend fun getChoreList(@Path("space")space:String):ChoreList
}