package com.depromeet.housekeeper.ui.settings

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.model.request.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val _alarmInfo: MutableStateFlow<Alarm?> = MutableStateFlow(null)
    val alarmInfo: StateFlow<Alarm?> = _alarmInfo

    init {
        getAlarmInfo()
    }

    private fun getAlarmInfo() {
        viewModelScope.launch {
            userRepository.getAlarmInfo().collectLatest {
                val result = receiveApiResult(it)
                if (result != null) {
                    _alarmInfo.value = result
                }
            }
        }
    }

    fun setAlarm(index: Int, value: Boolean) {
        viewModelScope.launch {
            if (index == 0) {
                _alarmInfo.value?.let { Alarm(value, it.notCompleteStatus) }
                    ?.let {
                        userRepository.setAlarm(it).collectLatest { data ->
                            val result = receiveApiResult(data)
                            if (result != null) {
                                getAlarmInfo()
                            }
                        }
                    }
            } else {
                _alarmInfo.value?.let { Alarm(it.scheduledTimeStatus, value) }
                    ?.let {
                        userRepository.setAlarm(it).collectLatest { data ->
                            val result = receiveApiResult(data)
                            if (result != null) {
                                getAlarmInfo()
                            }
                        }
                    }
            }

        }
    }
}
