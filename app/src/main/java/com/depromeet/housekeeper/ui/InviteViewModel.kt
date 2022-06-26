package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.local.PrefsManager
import com.depromeet.housekeeper.model.BuildTeam
import com.depromeet.housekeeper.model.InviteFailedResponse
import com.depromeet.housekeeper.model.enums.InviteViewType
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class InviteViewModel : ViewModel() {
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

    private val _errorBody: MutableStateFlow<InviteFailedResponse?> =
        MutableStateFlow(null)
    val errorBody: StateFlow<InviteFailedResponse?>
        get() = _errorBody

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


    fun setCode(teamName: String) {
        viewModelScope.launch {
            Repository.buildTeam(
                buildTeam = BuildTeam(teamName)
            ).runCatching {
                collect {
                    _inviteCode.value = it.inviteCode
                    PrefsManager.setHasTeam(true)
                }
            }
                .onFailure {
                    _networkError.value = true
                    //TODO 레이아웃 네트워크 처리
                }
        }
    }

    fun getInviteCodeResponse(){
        viewModelScope.launch {
            Repository.getInviteCode(
            ).runCatching {
                collect {
                    _groupName.value = it.teamName
                    _inviteCode.value = it.inviteCode
                    val str = it.inviteCodeExpirationDateTime
                    val arr = str.split("T")
                    val arrDate = arr[0].split("-")
                    val arrTime = arr[1].split(":")
                    _inviteCodeValidPeriod.value = "${arrDate[0]}년 ${arrDate[1]}월 ${arrDate[2]}일 ${arrTime[0]}시 ${arrTime[1]}분"
                }
            }
                .onFailure {
                    _networkError.value = true
                }
        }
    }
}