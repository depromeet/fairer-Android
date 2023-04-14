package com.depromeet.housekeeper.ui.statistics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemStatisticsBinding
import com.depromeet.housekeeper.model.response.StatsStatus

class MonthlyStatsAdapter : RecyclerView.Adapter<MonthlyStatsAdapter.ViewHolder>() {
    private var list: List<StatsStatus> = listOf()

    inner class ViewHolder(val binding: ItemStatisticsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StatsStatus) {
            //todo
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonthlyStatsAdapter.ViewHolder {
        return ViewHolder(ItemStatisticsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MonthlyStatsAdapter.ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(statsList: List<StatsStatus>) {
        this.list = statsList
    }
}