package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.AddHouseWorkResponse
import com.depromeet.housekeeper.model.AddHouseWorks
import com.depromeet.housekeeper.model.HouseWorks
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
  @GET("/api/houseworks")
  suspend fun getList(@Query("scheduledDate") scheduledDate: String): HouseWorks

  @POST("/api/houseworks")
  suspend fun createHouseWorks(@Body houseWorks: AddHouseWorks): AddHouseWorkResponse
}