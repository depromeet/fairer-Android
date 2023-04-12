package com.depromeet.housekeeper.ui.statistics

import androidx.fragment.app.viewModels
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun createView(binding: FragmentStatisticsBinding) {
    }

    override fun viewCreated() {
        initView()
    }

    fun initView() {
        binding.tvMonthTitle.text =
            String.format(getString(R.string.statistics_month_title, 1)) //todo  달 넣기
        binding.tvTitle.text =
            String.format(getString(R.string.statistics_title, "김민주")) //todo 이름 넣기
        binding.tvTotalChores.text = String.format(getString(R.string.statistics_total_chores, 16))
    }
}