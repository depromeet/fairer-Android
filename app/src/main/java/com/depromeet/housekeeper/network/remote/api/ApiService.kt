package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import retrofit2.http.*

interface ApiService {
  @POST("/api/houseworks")
  suspend fun createHouseWorks(@Body houseWorks: Chores): HouseWorkCreateResponse

  @GET("/api/houseworks")
  suspend fun getList(@Query("scheduledDate") scheduledDate: String): HouseWorks

  @GET("/api/preset")
  suspend fun getChoreList(): ChorePreset

  @GET("/api/houseworks/success/count")
  suspend fun getCompletedHouseWorkNumber(@Query("scheduledDate") scheduledDate: String): CompleteHouseWork

  @DELETE("/api/houseworks/{id}")
  suspend fun deleteHouseWork(@Path("id") id: Int)

  @PUT("/api/houseworks/{id}")
  suspend fun editHouseWork(@Path("id") id: Int, @Body chore: Chore): HouseWork

}