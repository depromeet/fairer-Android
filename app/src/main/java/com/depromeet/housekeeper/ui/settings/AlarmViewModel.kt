package com.depromeet.housekeeper.ui.settings

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.request.AlarmStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    init {
        getAlarmInformation()
    }

    private val _basicAlarm: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val basicAlarm: StateFlow<Boolean>
        get() = _basicAlarm

    private val _remindAlarm: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val remindAlarm: StateFlow<Boolean>
        get() = _remindAlarm

    private fun getAlarmInformation(){
        viewModelScope.launch{
            userRepository.getAlarmStatus().collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    Timber.d("$result")
                    _basicAlarm.value = result.scheduledTimeStatus
                    _remindAlarm.value = result.notCompleteStatus
                }
            }
        }
    }

    fun setAlarmInformation(basicAlarm:Boolean, remindAlarm:Boolean) {
        viewModelScope.launch {
            userRepository.setAlarmStatus(AlarmStatus(basicAlarm,remindAlarm)).collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    Timber.d("$result")
                }
            }
        }
    }

    fun setBasicAlarm(basicAlarm: Boolean){
        _basicAlarm.value = basicAlarm
    }

    fun setRemindAlarm(remindAlarm: Boolean){
        _remindAlarm.value = remindAlarm
    }

}