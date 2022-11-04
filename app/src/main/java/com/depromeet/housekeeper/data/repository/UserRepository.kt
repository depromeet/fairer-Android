package com.depromeet.housekeeper.data.repository


import com.depromeet.housekeeper.data.dataSource.RemoteDataSourceImpl
import com.depromeet.housekeeper.model.request.*
import com.depromeet.housekeeper.model.response.*
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

    suspend fun getAlarmStatus(): Flow<ApiResult<AlarmStatusResponse>> =
        remoteDataSource.getAlarmStatus()

    suspend fun setAlarmStatus(alarmStatus: AlarmStatus): Flow<ApiResult<AlarmStatusResponse>> =
        remoteDataSource.setAlarmStatus(alarmStatus)

}