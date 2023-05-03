package com.depromeet.housekeeper.ui.statistics.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.ItemRankBinding
import com.depromeet.housekeeper.model.ui.Ranker

class RankAdapter : RecyclerView.Adapter<RankAdapter.ViewHolder>() {
    private var list: List<Ranker> = listOf()

    inner class ViewHolder(private val binding: ItemRankBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("UseCompatLoadingForDrawables")
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
                    tvCnt.setTypeface(null, Typeface.BOLD)
                }

                when (rank.rank) {
                    1 -> {
                        ivRank.setImageDrawable(context.getDrawable(R.drawable.ic_crown))
                        bgView.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.highlight))
                        setRankerMargin(ivRank, context, R.dimen.item_rank_bottom_margin_1)
                        tvName.setTextColor(context.getColor(R.color.white))
                        tvCnt.setTextColor(context.getColor(R.color.white))
                    }
                    2 -> {
                        ivRank.setImageDrawable(context.getDrawable(R.drawable.ic_2))
                        bgView.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.positive_10))
                        setRankerMargin(ivRank, context, R.dimen.item_rank_bottom_margin_2)
                        tvCnt.setTextColor(context.getColor(R.color.gray_800))
                    }
                    3 -> {
                        ivRank.setImageDrawable(context.getDrawable(R.drawable.ic_3))
                        bgView.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.positive_0))
                        setRankerMargin(ivRank, context, R.dimen.item_rank_bottom_margin_3)
                        tvCnt.setTextColor(context.getColor(R.color.gray_800))
                    }
                }
            }
        }

        private fun setRankerMargin(target: View, context: Context, dimen: Int){
            val params = target.layoutParams as ConstraintLayout.LayoutParams
            params.bottomMargin = context.resources.getDimensionPixelSize(dimen)
            target.layoutParams = params
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