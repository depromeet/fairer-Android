package com.depromeet.housekeeper.ui.statistics

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.StatisticsRepository
import com.depromeet.housekeeper.model.response.HouseWorkStatsMember
import com.depromeet.housekeeper.model.response.HouseWorkStatsResponse
import com.depromeet.housekeeper.model.response.StatsStatus
import com.depromeet.housekeeper.model.ui.Stats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statsRepository: StatisticsRepository
) : BaseViewModel() {

    private val _statsFlow: MutableSharedFlow<List<Stats>> = MutableSharedFlow()
    val statsFlow get() = _statsFlow

    private val _statsList: MutableList<Stats> = mutableListOf()
    val statsList: List<Stats> get() = _statsList

    private val _rank: MutableStateFlow<List<HouseWorkStatsMember>> = MutableStateFlow(listOf())
    val rank: StateFlow<List<HouseWorkStatsMember>> get() = _rank

    private val _totalChoreCnt: MutableStateFlow<Int> = MutableStateFlow(0)
    val totalChoreCnt: StateFlow<Int> get() = _totalChoreCnt

    /**
     * Network Communication
     */
    fun getStatistics(yearMonth: String) {
        viewModelScope.launch {
            statsRepository.getStatistics(yearMonth).collectLatest {
                val result = receiveApiResult(it) ?: return@collectLatest

                _totalChoreCnt.value = result.statisticsList.size
                Timber.d("totalChoreCnt: ${_totalChoreCnt.value}")

                result.statisticsList.forEach {  status ->
                    statsRepository.getHoseWorkStatistics(status.houseWorkName, yearMonth).collectLatest {
                        val result = receiveApiResult(it) ?: return@collectLatest
                        val stats = Stats(
                            houseWorkName = status.houseWorkName,
                            totalCount = status.houseWorkCount,
                            members = result.houseWorkStatisticsList,)
                        _statsList.add(stats)
                        _statsFlow.emit(statsList)
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