package com.depromeet.housekeeper.ui.statistics.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.ItemRankBinding
import com.depromeet.housekeeper.model.ui.Ranker

class RankAdapter : RecyclerView.Adapter<RankAdapter.ViewHolder>() {
    private var list: List<Ranker> = listOf()

    inner class ViewHolder(private val binding: ItemRankBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(ranker: Ranker){
            val context = binding.root.context
            binding.apply {
                Glide.with(binding.root.context)
                    .load(ranker.member.profilePath)
                    .error(R.drawable.ic_profile1)
                    .circleCrop()
                    .into(ivMember)
                tvName.text = ranker.member.memberName
                tvCnt.text = String.format(context.getString(R.string.statistics_count, ranker.houseWorkCnt))
                rankNum = ranker.rank
                if (ranker.rank <= 3){
                    isRanker = true
                    tvCnt.setTypeface(null, Typeface.BOLD)
                }

                when (ranker.rank) {
                    1 -> {
                        ivRank.setImageDrawable(context.getDrawable(R.drawable.ic_crown))
                        bgView.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.highlight))
                        setBottomMargin(ivRank, context, R.dimen.item_rank_bottom_margin_1)
                        tvName.setTextColor(context.getColor(R.color.white))
                        tvCnt.setTextColor(context.getColor(R.color.white))
                    }
                    2 -> {
                        ivRank.setImageDrawable(context.getDrawable(R.drawable.ic_2))
                        bgView.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.positive_10))
                        setBottomMargin(ivRank, context, R.dimen.item_rank_bottom_margin_2)
                        tvCnt.setTextColor(context.getColor(R.color.gray_800))
                        setTopMargin(bgView, context, R.dimen.item_rank_top_margin_2)
                    }
                    3 -> {
                        ivRank.setImageDrawable(context.getDrawable(R.drawable.ic_3))
                        bgView.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.positive_0))
                        setBottomMargin(ivRank, context, R.dimen.item_rank_bottom_margin_3)
                        tvCnt.setTextColor(context.getColor(R.color.gray_800))
                        setTopMargin(bgView, context, R.dimen.item_rank_top_margin_3)
                    }
                }
            }
        }

        private fun setBottomMargin(target: View, context: Context, dimen: Int){
            val params = target.layoutParams as ConstraintLayout.LayoutParams
            params.bottomMargin = context.resources.getDimensionPixelSize(dimen)
            target.layoutParams = params
        }

        private fun setTopMargin(target: View, context: Context, dimen: Int){
            val params = target.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = context.resources.getDimensionPixelSize(dimen)
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