package com.depromeet.housekeeper.ui.statistics

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.StatisticsRepository
import com.depromeet.housekeeper.model.response.HouseWorkStatsMember
import com.depromeet.housekeeper.model.response.HouseWorkStatsResponse
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

    private val _rank: MutableStateFlow<List<HouseWorkStatsMember>> = MutableStateFlow(listOf())
    val rank: StateFlow<List<HouseWorkStatsMember>> get() = _rank

    private val _statsHouseWork: MutableStateFlow<MutableMap<String, List<HouseWorkStatsMember>>> =
        MutableStateFlow(mutableMapOf())
    val statsHouseWork: StateFlow<Map<String, List<HouseWorkStatsMember>>> get() = _statsHouseWork

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

    fun getRanking(yearMonth: String) {
        viewModelScope.launch {
            statsRepository.getStatisticsRanking(yearMonth).collectLatest {
                val result = receiveApiResult(it) ?: return@collectLatest

                _rank.value = result.houseWorkStatisticsList
            }
        }
    }

    fun getHouseWorkStatistics(houseWorkName: String, yearMonth: String) {
        viewModelScope.launch {
            statsRepository.getHoseWorkStatistics(houseWorkName, yearMonth).collectLatest {
                val result = receiveApiResult(it) ?: return@collectLatest

                _statsHouseWork.value[houseWorkName] = result.houseWorkStatisticsList.sortedByDescending { it.houseWorkCount }
            }
        }
    }
}