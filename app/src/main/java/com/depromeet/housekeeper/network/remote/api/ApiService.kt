package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.depromeet.housekeeper.model.HouseWorks
import retrofit2.http.Query


interface ApiService {

  @POST("/api/houseworks")
  suspend fun createHouseWorks(@Body houseWorks: Chores): HouseWorkCreateResponse

  @GET("/api/houseworks")
  suspend fun getList(@Query("scheduledDate") scheduledDate: String): HouseWorks
}