package com.depromeet.housekeeper.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.request.BuildTeam
import com.depromeet.housekeeper.model.enums.InviteViewType
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _viewType: MutableStateFlow<InviteViewType> =
        MutableStateFlow(InviteViewType.DEFAULT)
    val viewType: StateFlow<InviteViewType>
        get() = _viewType

    fun setViewType(viewType: InviteViewType) {
        _viewType.value = viewType
    }

    private val _groupName: MutableStateFlow<String> =
        MutableStateFlow("즐거운 우리집")
    val groupName: StateFlow<String>
        get() = _groupName

    fun setGroupName(groupName: String) {
        _groupName.value = groupName
    }

    private val _inviteCode: MutableStateFlow<String> =
        MutableStateFlow("")
    val inviteCode: StateFlow<String>
        get() = _inviteCode

    private val _inviteCodeValidPeriod: MutableStateFlow<String> =
        MutableStateFlow("")
    val inviteCodeValidPeriod: StateFlow<String>
        get() = _inviteCodeValidPeriod

    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError


    /**
     * Network Communication
     */

    fun setCode(teamName: String) {
        viewModelScope.launch {
            userRepository.buildTeam(
                buildTeam = BuildTeam(teamName)
            ).runCatching {
                collect {
                    _inviteCode.value = it.inviteCode
                    getInviteCodeResponse()
                    PrefsManager.setHasTeam(true)
                }
            }
                .onFailure {
                    _networkError.value = true
                    //TODO 레이아웃 네트워크 처리
                }
        }
    }

    fun getInviteCodeResponse() {
        viewModelScope.launch {
            userRepository.getInviteCode(
            ).runCatching {
                collect {
                    _groupName.value = it.teamName
                    _inviteCode.value = it.inviteCode
                    val str = it.inviteCodeExpirationDateTime
                    val arr = str.split("T")
                    val arrDate = arr[0].split("-")
                    val arrTime = arr[1].split(":")
                    _inviteCodeValidPeriod.value =
                        "${arrDate[0]}년 ${arrDate[1]}월 ${arrDate[2]}일 ${arrTime[0]}시 ${arrTime[1]}분"
                }
            }
                .onFailure {
                    _networkError.value = true
                }
        }
    }
}