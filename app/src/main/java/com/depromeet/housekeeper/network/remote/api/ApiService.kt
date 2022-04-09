package com.depromeet.housekeeper.network.remote.api

import com.depromeet.housekeeper.network.remote.model.SampleResponse
import retrofit2.http.GET


interface ApiService {
  //Sample API
  @GET("/v1/images/search")
  suspend fun getList(): List<SampleResponse>
}