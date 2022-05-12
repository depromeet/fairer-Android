package com.depromeet.housekeeper.network.remote.api


import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import com.depromeet.housekeeper.network.remote.model.LoginResponse
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

  @POST("/api/oauth/login")
  suspend fun googlelogin(@Header("Authorization")auth : String, @Body socialType : SocialType): LoginResponse

  @PATCH("/api/houseworks/{houseWorkId}")
  suspend fun updateChoreState(@Path("houseWorkId") houseWorkId: Int, @Body updateChoreBody : UpdateChoreBody): UpdateChoreResponse

}