package com.depromeet.housekeeper.ui.statistics

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentStatisticsBinding
import com.depromeet.housekeeper.ui.statistics.adapter.MonthlyStatsAdapter
import com.depromeet.housekeeper.ui.statistics.adapter.RankAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()
    private lateinit var rankAdapter: RankAdapter
    private lateinit var statsAdapter: MonthlyStatsAdapter

    override fun createView(binding: FragmentStatisticsBinding) {
    }

    override fun viewCreated() {
        setAdapter()
        initView()
        bindingVm()
    }

    fun setAdapter(){
        rankAdapter = RankAdapter()
        statsAdapter = MonthlyStatsAdapter()
    }

    fun initView() {
        val yearMonthFormat = SimpleDateFormat("yyyy-MM")
        val yearMonth = yearMonthFormat.format(Date())
        viewModel.getStatistics(yearMonth)
        viewModel.getRanking(yearMonth)

        val monthFormat = SimpleDateFormat("MM")
        val currentMonth: String = monthFormat.format(Date())
        binding.tvMonthTitle.text =
            String.format(getString(R.string.statistics_month_title, currentMonth))
        binding.tvTitle.text =
            String.format(getString(R.string.statistics_title, "김민주")) //todo 이름 넣기
        binding.tvTotalChores.text = String.format(getString(R.string.statistics_total_chores, 16))

        binding.rvRanking.adapter = rankAdapter
        binding.rvMonthlyStats.adapter = statsAdapter
    }

    fun bindingVm(){

        lifecycleScope.launchWhenStarted {
            viewModel.statsList.collect{
                statsAdapter.submitList(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.rank.collectLatest {
                rankAdapter.submitList(it)
            }
        }
    }
}