package com.depromeet.housekeeper.ui.statistics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemMemberStatisticsBinding
import com.depromeet.housekeeper.model.response.HouseWorkStatsMember

class StatsMemberAdapter(val list: List<HouseWorkStatsMember>) :
    RecyclerView.Adapter<StatsMemberAdapter.ViewHolder>()
{
    inner class ViewHolder(val binding: ItemMemberStatisticsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HouseWorkStatsMember) {
            binding.apply {
                imgUrl = item.member.profilePath
                name = item.member.memberName
                cnt = item.houseWorkCount
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMemberStatisticsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


}