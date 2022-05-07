package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.ChorePreset
import com.depromeet.housekeeper.model.CompleteHouseWork
import com.depromeet.housekeeper.model.HouseWorks
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
  //Sample API
  @GET("/api/houseworks")
  suspend fun getList(@Query("scheduledDate") scheduledDate: String): HouseWorks

  @GET("/api/preset")
  suspend fun getChoreList(): ChorePreset

  //Sample API
  @GET("/api/houseworks/success/count")
  suspend fun getCompletedHouseWorkNumber(@Query("scheduledDate") scheduledDate: String): CompleteHouseWork

}