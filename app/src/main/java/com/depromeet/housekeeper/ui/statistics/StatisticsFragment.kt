package com.depromeet.housekeeper.ui.statistics

import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentStatisticsBinding
import com.depromeet.housekeeper.ui.statistics.adapter.StatsAdapter
import com.depromeet.housekeeper.ui.statistics.adapter.RankAdapter
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()
    private lateinit var rankAdapter: RankAdapter
    private lateinit var statsAdapter: StatsAdapter

    override fun createView(binding: FragmentStatisticsBinding) {
    }

    override fun viewCreated() {
        setAdapter()
        initView()
        bindingVm()
    }

    fun setAdapter(){
        rankAdapter = RankAdapter()
        statsAdapter = StatsAdapter(mutableListOf())

        binding.rvRanking.adapter = rankAdapter
        //binding.rvRanking.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMonthlyStats.adapter = statsAdapter
        binding.rvMonthlyStats.layoutManager = LinearLayoutManager(requireContext())
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
            HtmlCompat.fromHtml(getString(R.string.statistics_title, PrefsManager.userName), HtmlCompat.FROM_HTML_MODE_LEGACY) //todo 이름 넣기


    }

    fun bindingVm(){

        lifecycleScope.launchWhenStarted {
            viewModel.statsFlow.collect{
                Timber.d("stats: ${it}, ${statsAdapter.itemCount}")
                statsAdapter.submitItem(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.rank.collectLatest {
                rankAdapter.submitList(ArrayList(it))
                rankAdapter.notifyDataSetChanged()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.totalChoreCnt.collectLatest {
                setChoreCntTv(it)
            }
        }
    }

    private fun setChoreCntTv(cnt: Int){
        binding.tvTotalChores.text = HtmlCompat.fromHtml(getString(R.string.statistics_total_chores, cnt), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

}