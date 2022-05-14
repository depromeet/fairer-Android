package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import retrofit2.http.*
import com.depromeet.housekeeper.model.ChorePreset
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.CompleteHouseWork
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.UpdateChoreBody
import com.depromeet.housekeeper.model.UpdateChoreResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
  @POST("/api/houseworks")
  suspend fun createHouseWorks(@Header("AUTHORIZATION") authorization: String, @Body houseWorks: Chores): HouseWorkCreateResponse

  @GET("/api/houseworks")
  suspend fun getList(@Header("AUTHORIZATION") authorization: String, @Query("scheduledDate") scheduledDate: String): HouseWorks

  @GET("/api/preset")
  suspend fun getChoreList(@Header("AUTHORIZATION") authorization: String): ChorePreset

  @GET("/api/houseworks/success/count")
  suspend fun getCompletedHouseWorkNumber(@Header("AUTHORIZATION") authorization: String, @Query("scheduledDate") scheduledDate: String): CompleteHouseWork

  @DELETE("/api/houseworks/{id}")
  suspend fun deleteHouseWork(@Header("AUTHORIZATION") authorization: String, @Path("id") id: Int)

  @PUT("/api/houseworks/{id}")
  suspend fun editHouseWork(@Header("AUTHORIZATION") authorization: String, @Path("id") id: Int, @Body chore: Chore): HouseWork

  @PATCH("/api/houseworks/{houseWorkId}")
  suspend fun updateChoreState(@Header("AUTHORIZATION") authorization: String, @Path("houseWorkId") houseWorkId: Int, @Body updateChoreBody : UpdateChoreBody): UpdateChoreResponse
}