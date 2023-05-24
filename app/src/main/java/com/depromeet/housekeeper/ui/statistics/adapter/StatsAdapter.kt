package com.depromeet.housekeeper.ui.statistics.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.ItemStatisticsBinding
import com.depromeet.housekeeper.model.ui.Stats
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.gms.common.internal.ResourceUtils
import timber.log.Timber


class StatsAdapter(private var list: MutableList<Stats> ) : RecyclerView.Adapter<StatsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemStatisticsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Stats) {
            Timber.d("stats: ${item}")
            binding.apply {
                houseWorkName = item.houseWorkName.ifBlank { "기타" }
                val totalCnt = binding.root.context.getString(R.string.statistics_total_complete, item.totalCount)
                tvTotalChores.text = String.format(totalCnt)
                val flexboxLayoutManager = FlexboxLayoutManager(binding.root.context).apply {
                    flexDirection = FlexDirection.ROW
                    justifyContent = JustifyContent.FLEX_START
                }
                rvMemberList.layoutManager = flexboxLayoutManager
                rvMemberList.adapter = StatsMemberAdapter(item.members)
                var isDroppedVar = false
                isDropped = isDroppedVar

                clTotalChores.setOnClickListener {
                    isDroppedVar = !isDroppedVar
                    isDropped = isDroppedVar
                }
                btnDrop.setOnClickListener {
                    isDroppedVar = !isDroppedVar
                    isDropped = isDroppedVar
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemStatisticsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitItem(stats: Stats){
        list.add( stats)
        notifyItemInserted(list.size-1)
    }
}