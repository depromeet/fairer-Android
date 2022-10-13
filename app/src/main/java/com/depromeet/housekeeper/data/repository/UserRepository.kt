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
    suspend fun getGoogleLogin(socialType: SocialType): Flow<LoginResponse> =
        remoteDataSource.getGoogleLogin(socialType)

    suspend fun logout(): Flow<ApiResult<Unit>> = remoteDataSource.logout()

    /**
     * teams
     */
    suspend fun buildTeam(buildTeam: BuildTeam) : Flow<BuildTeamResponse> =
        remoteDataSource.buildTeam(buildTeam)

    suspend fun getTeam(): Flow<ApiResult<Groups>> = remoteDataSource.getTeam()

    suspend fun getInviteCode(): Flow<GetInviteCode> = remoteDataSource.getInviteCode()

    suspend fun updateTeam(teamName: BuildTeam): Flow<TeamUpdateResponse> =
        remoteDataSource.updateTeam(teamName)

    suspend fun joinTeam(inviteCode: JoinTeam): Flow<JoinTeamResponse> =
        remoteDataSource.joinTeam(inviteCode)

    suspend fun leaveTeam() = remoteDataSource.leaveTeam()
    /**
     * members
     */
    suspend fun getProfileImages(): Flow<ProfileImages> =
        remoteDataSource.getProfileImages()

    suspend fun updateMember(updateMember: UpdateMember): Flow<UpdateMemberResponse> =
        remoteDataSource.updateMember(updateMember)

    suspend fun getMe(): Flow<ProfileData> = remoteDataSource.getMe()

    suspend fun updateMe(editProfileModel: EditProfileModel): Flow<EditResponseBody> =
        remoteDataSource.updateMe(editProfileModel)

    /**
    fcm
     */
    suspend fun saveToken(token: Token): Flow<Unit> =
        remoteDataSource.saveToken(token)

    suspend fun sendMessage(message: Message): Flow<Message> =
        remoteDataSource.sendMessage(message)

}