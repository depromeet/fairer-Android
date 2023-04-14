package com.depromeet.housekeeper.ui.statistics

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.StatisticsRepository
import com.depromeet.housekeeper.model.response.HouseWorkStatsMember
import com.depromeet.housekeeper.model.response.HouseWorkStatsResponse
import com.depromeet.housekeeper.model.response.StatsStatus
import com.depromeet.housekeeper.model.ui.Stats
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

    private val _statsList: MutableStateFlow<MutableList<Stats>> = MutableStateFlow(mutableListOf())
    val statsList: StateFlow<List<Stats>> get() = _statsList

    private val _rank: MutableStateFlow<List<HouseWorkStatsMember>> = MutableStateFlow(listOf())
    val rank: StateFlow<List<HouseWorkStatsMember>> get() = _rank

    /**
     * Network Communication
     */
    fun getStatistics(yearMonth: String) {
        viewModelScope.launch {
            statsRepository.getStatistics(yearMonth).collectLatest {
                val result = receiveApiResult(it) ?: return@collectLatest

                result.statisticsList.forEach {  status ->
                    statsRepository.getHoseWorkStatistics(status.houseWorkName, yearMonth).collectLatest {
                        val result = receiveApiResult(it) ?: return@collectLatest

                        val stats = Stats(
                            houseWorkName = status.houseWorkName,
                            totalCount = status.houseWorkCount,
                            members = result.houseWorkStatisticsList,)
                        _statsList.value.add(stats)
                    }
                }
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

}