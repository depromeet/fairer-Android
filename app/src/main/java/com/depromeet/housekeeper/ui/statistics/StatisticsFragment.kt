package com.depromeet.housekeeper.ui.statistics

import androidx.fragment.app.viewModels
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun createView(binding: FragmentStatisticsBinding) {
    }

    override fun viewCreated() {
        initView()
    }

    fun initView() {
        val dateFormat = SimpleDateFormat("MM")
        val currentMonth: String = dateFormat.format(Date())
        binding.tvMonthTitle.text =
            String.format(getString(R.string.statistics_month_title, currentMonth))
        binding.tvTitle.text =
            String.format(getString(R.string.statistics_title, "김민주")) //todo 이름 넣기
        binding.tvTotalChores.text = String.format(getString(R.string.statistics_total_chores, 16))
    }
}