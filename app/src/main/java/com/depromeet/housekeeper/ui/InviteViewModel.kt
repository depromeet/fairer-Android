package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.model.enums.InviteViewType
import com.depromeet.housekeeper.network.remote.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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

    fun setGroupName(groupName : String){
        _groupName.value = groupName
    }

    private val _inviteCode: MutableStateFlow<String> =
        MutableStateFlow("ABCDEFG")
    val inviteCode: StateFlow<String>
        get() = _inviteCode

    private val _inviteCodeValidPeriod: MutableStateFlow<String> =
        MutableStateFlow("2022년 6월 20일 18시 23분")
    val inviteCodeValidPeriod: StateFlow<String>
        get() = _inviteCodeValidPeriod

    private val _networkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val networkError: StateFlow<Boolean>
        get() = _networkError

    fun setInviteCodeValidPeriod(){
        _inviteCodeValidPeriod.value = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
    }

    fun setCode(teamName : String){
        viewModelScope.launch {
            Repository.buildTeam(teamName = teamName
            ).runCatching {
                collect {
                    _inviteCode.value = it.inviteCode
                }
            }
                .onFailure {
                    _networkError.value = true
                    //TODO 레이아웃 네트워크 처리
                }
        }
    }
}