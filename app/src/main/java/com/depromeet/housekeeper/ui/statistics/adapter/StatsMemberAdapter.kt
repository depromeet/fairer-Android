package com.depromeet.housekeeper.ui.statistics.adapter


import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.ItemMemberStatisticsBinding
import com.depromeet.housekeeper.model.response.HouseWorkStatsMember

class StatsMemberAdapter(val list: List<HouseWorkStatsMember>) :
    RecyclerView.Adapter<StatsMemberAdapter.ViewHolder>()
{
    inner class ViewHolder(val binding: ItemMemberStatisticsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HouseWorkStatsMember) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(item.member.profilePath)
                    .error(R.drawable.ic_profile1)
                    .circleCrop()
                    .into(ivMember)
                name = item.member.memberName
                cnt = item.houseWorkCount

                if (list[0] == item){ // 백엔드에서 sort해서 제공해줌
                    tvCnt.setTextColor(ContextCompat.getColor(binding.root.context, R.color.highlight))
                    tvCnt.setTypeface(tvCnt.typeface, Typeface.BOLD)
                }
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