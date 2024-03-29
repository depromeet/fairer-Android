package com.depromeet.housekeeper.ui.join

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.Assignee
import com.depromeet.housekeeper.model.request.Message
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupInfoViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    init {
        getGroupName()
    }

    private val _groupName: MutableStateFlow<String> = MutableStateFlow("즐거운 우리집")
    val groupName: StateFlow<String>
        get() = _groupName

    private val _groups: MutableStateFlow<ArrayList<Assignee>> = MutableStateFlow(arrayListOf())
    val groups: MutableStateFlow<ArrayList<Assignee>>
        get() = _groups


    private fun deleteMyInfo() {
        var temp: Assignee? = null
        _groups.value.map {
            if (it.memberId == PrefsManager.memberId) {
                temp = it
            }
        }
        _groups.value.remove(temp)
    }

    /**
     * Network Communication
     */

    private fun getGroupName() {
        viewModelScope.launch {
            userRepository.getTeam().collectLatest {
                val result = receiveApiResult(it)
                if (result != null){
                    _groupName.value = result.teamName
                    _groups.value = result.members as ArrayList<Assignee>
                    deleteMyInfo()
                }
            }
        }
    }

    fun sendAddMemberFCM(context: Context) {
        // 그룹 참여 알림 요청
        val message = Message(
            body = String.format(
                context.getString(R.string.fcm_add_member_title),
                PrefsManager.userName,
                _groupName.value
            ),
            memberId = PrefsManager.memberId,
            title = context.getString(R.string.fcm_add_member_title)
        )

        viewModelScope.launch {
//            Repository.sendMessage(message = message).runCatching {
//                collect {
//                    Timber.d(it.toString())
//                }
//            }
        }
    }
}