package com.depromeet.housekeeper.network.remote.api


import com.depromeet.housekeeper.model.ChorePreset
import com.depromeet.housekeeper.model.Chores
import com.depromeet.housekeeper.model.CompleteHouseWork
import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.model.UpdateChoreBody
import com.depromeet.housekeeper.model.UpdateChoreResponse
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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