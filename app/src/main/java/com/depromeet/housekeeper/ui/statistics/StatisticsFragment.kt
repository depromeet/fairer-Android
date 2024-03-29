package com.depromeet.housekeeper.ui.statistics

import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentStatisticsBinding
import com.depromeet.housekeeper.ui.custom.dialog.MonthPickerDialog
import com.depromeet.housekeeper.ui.statistics.adapter.StatsAdapter
import com.depromeet.housekeeper.ui.statistics.adapter.RankAdapter
import com.depromeet.housekeeper.util.DateUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
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

    private fun setAdapter(){
        rankAdapter = RankAdapter()
        statsAdapter = StatsAdapter(mutableListOf())

        binding.rvRanking.adapter = rankAdapter
        binding.rvRanking.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvMonthlyStats.adapter = statsAdapter
        binding.rvMonthlyStats.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initView() {
        setClickListener()
    }

    private fun bindingVm(){
        lifecycleScope.launchWhenStarted {
            viewModel.currentDate.collect {
                val yearMonth = DateUtil.getHyphenYearMonthString(it)
                viewModel.getStatistics(yearMonth)
                viewModel.getRanking(yearMonth)
                Timber.d("yearMonth = ${yearMonth}")

                val currentMonth = DateUtil.getMonthString(it)
                binding.tvMonthTitle.text =
                    String.format(getString(R.string.statistics_month_title, currentMonth))
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.statsFlow.collect{
                Timber.d("stats: ${it}, ${statsAdapter.itemCount}")
                statsAdapter.submitItem(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.rankFlow.collectLatest {
                if (it.isEmpty()){
                    setTitleTv(null)
                } else {
                    val names: List<String> = it.filter { ranker ->  ranker.rank == 1}.map {
                        it.member.memberName
                    }
                    Timber.d("names : $names")
                    setTitleTv(names.toString().substring(1, names.toString().length-1))
                }
                rankAdapter.submitList(ArrayList(it))
                Timber.d("rank: ${it}")
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.totalChoreCnt.collectLatest {
                setChoreCntTv(it)
            }
        }
    }

    private fun setClickListener(){
        binding.tvMonthTitle.setOnClickListener {
            createMonthPickerDialog()
        }

        binding.btnMonthLeft.bringToFront()
        binding.btnMonthLeft.setOnClickListener {
            statsAdapter.clearList()
            viewModel.setCurrentDate(-1)
        }

        binding.btnMonthRight.bringToFront()
        binding.btnMonthRight.setOnClickListener {
            statsAdapter.clearList()
            viewModel.setCurrentDate(1)
        }
    }

    private fun setChoreCntTv(cnt: Int){
        binding.tvTotalChores.text = HtmlCompat.fromHtml(getString(R.string.statistics_total_chores, cnt), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setTitleTv(name: String?){
        binding.tvTitle.text =
            if (name.isNullOrBlank()) getString(R.string.statistics_title_empty)
            else HtmlCompat.fromHtml(getString(R.string.statistics_title, name), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun createMonthPickerDialog(){
        val dialog = MonthPickerDialog(viewModel.currentDate.value){
            viewModel.setCurrentDate(it)
            statsAdapter.clearList()
        }
        dialog.show(childFragmentManager, dialog.tag)
    }

}