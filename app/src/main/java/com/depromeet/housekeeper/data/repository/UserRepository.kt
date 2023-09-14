package com.depromeet.housekeeper.data.repository


import com.depromeet.housekeeper.data.dataSource.RemoteDataSourceImpl
import com.depromeet.housekeeper.model.request.Alarm
import com.depromeet.housekeeper.model.request.BuildTeam
import com.depromeet.housekeeper.model.request.EditProfileModel
import com.depromeet.housekeeper.model.request.JoinTeam
import com.depromeet.housekeeper.model.request.Message
import com.depromeet.housekeeper.model.request.SocialType
import com.depromeet.housekeeper.model.request.Token
import com.depromeet.housekeeper.model.request.UpdateMember
import com.depromeet.housekeeper.model.response.ApiResult
import com.depromeet.housekeeper.model.response.BuildTeamResponse
import com.depromeet.housekeeper.model.response.EditResponseBody
import com.depromeet.housekeeper.model.response.GetInviteCode
import com.depromeet.housekeeper.model.response.Groups
import com.depromeet.housekeeper.model.response.JoinTeamResponse
import com.depromeet.housekeeper.model.response.LoginResponse
import com.depromeet.housekeeper.model.response.ProfileData
import com.depromeet.housekeeper.model.response.ProfileImages
import com.depromeet.housekeeper.model.response.TeamUpdateResponse
import com.depromeet.housekeeper.model.response.UpdateMemberResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl
) {
    /**
     * oauth
     */
    suspend fun getGoogleLogin(socialType: SocialType): Flow<ApiResult<LoginResponse>> =
        remoteDataSource.getGoogleLogin(socialType)

    suspend fun logout(): Flow<ApiResult<Unit>> = remoteDataSource.logout()

    suspend fun signOut() : Flow<ApiResult<Unit>> = remoteDataSource.signOut()
    /**
     * teams
     */
    suspend fun buildTeam(buildTeam: BuildTeam) : Flow<ApiResult<BuildTeamResponse>> =
        remoteDataSource.buildTeam(buildTeam)

    suspend fun getTeam(): Flow<ApiResult<Groups>> = remoteDataSource.getTeam()

    suspend fun getInviteCode(): Flow<ApiResult<GetInviteCode>> = remoteDataSource.getInviteCode()

    suspend fun updateTeam(teamName: BuildTeam): Flow<ApiResult<TeamUpdateResponse>> =
        remoteDataSource.updateTeam(teamName)

    suspend fun joinTeam(inviteCode: JoinTeam): Flow<ApiResult<JoinTeamResponse>> =
        remoteDataSource.joinTeam(inviteCode)

    suspend fun leaveTeam() = remoteDataSource.leaveTeam()
    /**
     * members
     */
    suspend fun getProfileImages(): Flow<ApiResult<ProfileImages>> =
        remoteDataSource.getProfileImages()

    suspend fun updateMember(updateMember: UpdateMember): Flow<ApiResult<UpdateMemberResponse>> =
        remoteDataSource.updateMember(updateMember)

    suspend fun getMe(): Flow<ApiResult<ProfileData>> = remoteDataSource.getMe()

    suspend fun updateMe(editProfileModel: EditProfileModel): Flow<ApiResult<EditResponseBody>> =
        remoteDataSource.updateMe(editProfileModel)

    /**
    fcm
     */
    suspend fun saveToken(token: Token): Flow<ApiResult<Unit>> =
        remoteDataSource.saveToken(token)

    suspend fun sendMessage(message: Message): Flow<ApiResult<Message>> =
        remoteDataSource.sendMessage(message)

    suspend fun getAlarmInfo(): Flow<ApiResult<Alarm>> = remoteDataSource.getAlarmInfo()
    suspend fun setAlarm(alarm: Alarm): Flow<ApiResult<Unit>> = remoteDataSource.setAlarm(alarm)
}
