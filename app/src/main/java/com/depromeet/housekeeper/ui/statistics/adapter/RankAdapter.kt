package com.depromeet.housekeeper.ui.statistics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.ItemRankBinding
import com.depromeet.housekeeper.model.ui.Ranker
import com.depromeet.housekeeper.util.dp2px

class RankAdapter : RecyclerView.Adapter<RankAdapter.ViewHolder>() {
    private var list: List<Ranker> = listOf()

    inner class ViewHolder(private val binding: ItemRankBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rank: Ranker){
            val context = binding.root.context
            binding.apply {
                Glide.with(binding.root.context)
                    .load(rank.member.profilePath)
                    .error(R.drawable.ic_profile1)
                    .circleCrop()
                    .into(ivMember)
                tvName.text = rank.member.memberName
                tvCnt.text = String.format(context.getString(R.string.statistics_count, rank.houseWorkCnt))
                if (rank.rank <= 3){
                    isRanker = true
                }
                tvRank.text = rank.rank.toString()
                when (rank.rank) {
                    1 -> {
                        bgView.setBackgroundColor(context.getColor(R.color.highlight))
                        val params = tvRank.layoutParams as ConstraintLayout.LayoutParams
                        params.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.item_rank_bottom_margin_1)
                        tvRank.layoutParams = params
                    }
                    2 -> {
                        bgView.setBackgroundColor(context.getColor(R.color.positive_10))
                        val params = tvRank.layoutParams as ConstraintLayout.LayoutParams
                        params.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.item_rank_bottom_margin_2)
                        tvRank.layoutParams = params
                    }
                    3 -> {
                        bgView.setBackgroundColor(context.getColor(R.color.positive_0))
                        val params = tvRank.layoutParams as ConstraintLayout.LayoutParams
                        params.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.item_rank_bottom_margin_3)
                        tvRank.layoutParams = params
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankAdapter.ViewHolder {
        return ViewHolder(ItemRankBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RankAdapter.ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(rankList: List<Ranker>){
        this.list = rankList
    }

}