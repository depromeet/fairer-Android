package com.depromeet.housekeeper.network.remote.api


import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.network.remote.model.HouseWorkCreateResponse
import com.depromeet.housekeeper.network.remote.model.LoginResponse
import retrofit2.http.*

interface ApiService {
  @POST("/api/houseworks")
  suspend fun createHouseWorks(@Body houseWorks: Chores): HouseWorkCreateResponse

  @GET("/api/houseworks")
  suspend fun getList(@Query("scheduledDate") scheduledDate: String): List<HouseWorks>

  @GET("/api/preset")
  suspend fun getChoreList(): ChorePreset

  @GET("/api/houseworks/success/count")
  suspend fun getCompletedHouseWorkNumber(@Query("scheduledDate") scheduledDate: String): CompleteHouseWork

  @POST("/api/oauth/login")
  suspend fun googleLogin(@Header("Authorization")auth : String, @Body socialType : SocialType): LoginResponse
  
  @DELETE("/api/houseworks/{id}")
  suspend fun deleteHouseWork(@Path("id") id: Int)

  @PUT("/api/houseworks/{id}")
  suspend fun editHouseWork(@Path("id") id: Int, @Body chore: Chore): HouseWork

  @PATCH("/api/houseworks/{houseWorkId}")
  suspend fun updateChoreState(@Path("houseWorkId") houseWorkId: Int, @Body updateChoreBody : UpdateChoreBody): UpdateChoreResponse

  @POST("/api/oauth/logout")
  suspend fun logout(@Header("Authorization")auth : String)

  @POST("/api/teams")
  suspend fun buildTeam(@Body buildTeam : BuildTeam) : BuildTeamResponse

  @GET("/api/teams/my")
  suspend fun getTeamData(): Groups

  @GET("/api/member/profile-image")
  suspend fun getProfileImages() : ProfileImages

  @PATCH("/api/member")
  suspend fun updateMember(@Body updateMember: UpdateMember) : UpdateMemberResponse

  @GET("/api/teams/invite-codes")
  suspend fun getInviteCode() : GetInviteCode

  @PATCH("/api/teams")
  suspend fun updateTeam(@Body teamName : BuildTeam) : TeamUpdateResponse

  @POST("/api/teams/join")
  suspend fun joinTeam(@Body inviteCode: JoinTeam) : JoinTeamResponse

}