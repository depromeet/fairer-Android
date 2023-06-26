package com.depromeet.housekeeper.ui.statistics

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.StatisticsRepository
import com.depromeet.housekeeper.model.ui.Ranker
import com.depromeet.housekeeper.model.ui.Stats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statsRepository: StatisticsRepository
) : BaseViewModel() {

    private val _statsFlow: MutableSharedFlow<Stats> = MutableSharedFlow()
    val statsFlow get() = _statsFlow

    private val _rankFlow: MutableStateFlow<MutableList<Ranker>> = MutableStateFlow(mutableListOf())
    val rankFlow: StateFlow<List<Ranker>> get() = _rankFlow

    private val _totalChoreCnt: MutableStateFlow<Int> = MutableStateFlow(0)
    val totalChoreCnt: StateFlow<Int> get() = _totalChoreCnt

    private var _currentDate: MutableStateFlow<Date> = MutableStateFlow(Date())
    val currentDate: StateFlow<Date> get() = _currentDate

    fun setCurrentDate(mm: Int) {
        val nDate =
            Date(currentDate.value.time) //그냥 객체에서 mm 바꾸면 객체 주소는 그대로라 stateFlow update 안되어서 새로운 Date 객체 만듦
        nDate.month += mm
        _currentDate.value = nDate
        Timber.d("${currentDate.value.month}")
    }

    fun setCurrentDate(date: Date) {
        _currentDate.value = date
    }

    /**
     * Network Communication
     */
    fun getStatistics(yearMonth: String) {
        viewModelScope.launch {
            statsRepository.getStatistics(yearMonth).collectLatest {
                val result = receiveApiResult(it) ?: return@collectLatest

                _totalChoreCnt.value = result.statisticsList.size
                Timber.d("totalChoreCnt: ${_totalChoreCnt.value}")

                result.statisticsList.forEach { status ->
                    statsRepository.getHoseWorkStatistics(status.houseWorkName, yearMonth)
                        .collectLatest {
                            val result = receiveApiResult(it) ?: return@collectLatest
                            val stats = Stats(
                                houseWorkName = status.houseWorkName,
                                totalCount = status.houseWorkCount,
                                members = result.houseWorkStatisticsList,
                            )
                            _statsFlow.emit(stats)
                        }
                }
            }
        }
    }

    fun getRanking(yearMonth: String) {
        viewModelScope.launch {
            statsRepository.getStatisticsRanking(yearMonth).collectLatest {
                val result = receiveApiResult(it) ?: return@collectLatest

                val size = result.houseWorkStatisticsList.size
                val list = mutableListOf<Ranker>()
                val topList = result.houseWorkStatisticsList.map {
                    it.houseWorkCount
                }.distinct().take(3)
                Timber.d("ranking topList: $topList")

                var st = 0
                for (i: Int in topList.indices) {
                    var j: Int = st
                    while (j < size) {
                        if (result.houseWorkStatisticsList[j].houseWorkCount == topList[i]) {
                            list.add(
                                Ranker(
                                    i + 1,
                                    result.houseWorkStatisticsList[j].member,
                                    result.houseWorkStatisticsList[j].houseWorkCount
                                )
                            )
                        } else {
                            st = j
                            break
                        }
                        j++
                    }
                    if (j == size) st = size
                }

                Timber.d("ranking: ${list.size}, ${st}")

                for (i: Int in st until size) {
                    val cur = result.houseWorkStatisticsList[i]
                    val ranker = Ranker(
                        rank = 0,
                        member = cur.member,
                        houseWorkCnt = cur.houseWorkCount
                    )
                    list.add(ranker)
                }


                _rankFlow.emit(list)
            }
        }
    }

}