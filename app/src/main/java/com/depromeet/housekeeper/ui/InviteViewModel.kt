package com.depromeet.housekeeper.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class InviteViewModel : ViewModel() {
    private val _groupName: MutableStateFlow<String> =
        MutableStateFlow("즐거운 우리집")
    val groupName: StateFlow<String>
        get() = _groupName

    private val _inviteCode: MutableStateFlow<String> =
        MutableStateFlow("ABCDEFG")
    val inviteCode: StateFlow<String>
        get() = _inviteCode

    private val _inviteCodeValidPeriod: MutableStateFlow<String> =
        MutableStateFlow("2022년 6월 20 18시 23분")
    val inviteCodeValidPeriod: StateFlow<String>
        get() = _inviteCodeValidPeriod
}