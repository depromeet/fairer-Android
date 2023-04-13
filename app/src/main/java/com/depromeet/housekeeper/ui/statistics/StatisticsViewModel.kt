package com.depromeet.housekeeper.ui.statistics

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.StatisticsRepository
import com.depromeet.housekeeper.model.response.HouseWorkStatsStatus
import com.depromeet.housekeeper.model.response.StatsStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statsRepository: StatisticsRepository
) : BaseViewModel() {

    private val _statsList: MutableStateFlow<List<StatsStatus>> = MutableStateFlow(listOf())
    val statsList: StateFlow<List<StatsStatus>> get() = _statsList

    private val _statsHouseWork: MutableStateFlow<MutableMap<String, HouseWorkStatsStatus>> =
        MutableStateFlow(mutableMapOf())
    val statsHouseWork: StateFlow<Map<String, HouseWorkStatsStatus>> get() = _statsHouseWork

    /**
     * Network Communication
     */
    fun getStatistics(yearMonth: String) {
        viewModelScope.launch {
            statsRepository.getStatistics(yearMonth).collectLatest {
                val result = receiveApiResult(it) ?: return@collectLatest

                _statsList.value = result.statisticsList
            }
        }
    }

    fun getHouseWorkStatistics(houseWorkName: String, yearMonth: String) {
        viewModelScope.launch {
            statsRepository.getHoseWorkStatistics(houseWorkName, yearMonth).collectLatest {
                val result = receiveApiResult(it) ?: return@collectLatest

                //todo 데이터 받아와 처리
                _statsHouseWork.value[houseWorkName] = result.houseWorkStatisticsList[0]
            }
        }
    }
}