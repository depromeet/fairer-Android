package com.depromeet.housekeeper.ui.statistics

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun createView(binding: FragmentStatisticsBinding) {
    }

    override fun viewCreated() {
        initView()
        bindingVm()
    }

    fun initView() {
        val yearMonthFormat = SimpleDateFormat("yyyy-MM")
        viewModel.getStatistics(yearMonthFormat.format(Date()))

        val monthFormat = SimpleDateFormat("MM")
        val currentMonth: String = monthFormat.format(Date())
        binding.tvMonthTitle.text =
            String.format(getString(R.string.statistics_month_title, currentMonth))
        binding.tvTitle.text =
            String.format(getString(R.string.statistics_title, "김민주")) //todo 이름 넣기
        binding.tvTotalChores.text = String.format(getString(R.string.statistics_total_chores, 16))
    }

    fun bindingVm(){
        lifecycleScope.launchWhenStarted {
            viewModel.statsList.collect{
                // todo adapter list로 넣기
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.statsHouseWork.collectLatest {  }
        }
    }
}