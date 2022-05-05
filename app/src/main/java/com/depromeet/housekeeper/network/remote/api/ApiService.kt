package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.model.Chore
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import com.depromeet.housekeeper.network.remote.model.SampleResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {
  //Sample API
  @GET("/v1/images/search")
  suspend fun getList(): List<SampleResponse>

  @POST("/api/houseworks")
  suspend fun createHouseWorks(@Body houseWorks: List<Chore>): HouseWorkCreateResponse
}