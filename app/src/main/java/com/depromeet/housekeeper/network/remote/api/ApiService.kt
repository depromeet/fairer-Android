package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.HouseWorks
import com.depromeet.housekeeper.network.remote.model.SampleResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
  //Sample API
  @GET("/api/houseworks")
  suspend fun getList(@Query("scheduledDate") scheduledDate: String): List<HouseWorks>
}